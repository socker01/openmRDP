package cz.vutbr.fit.openmrdp.api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import cz.vutbr.fit.openmrdp.cache.ClientEntry;
import cz.vutbr.fit.openmrdp.communication.MessageReceiverImpl;
import cz.vutbr.fit.openmrdp.communication.MessageSenderImpl;
import cz.vutbr.fit.openmrdp.communication.MessageService;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.MessageDeserializeException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.logger.MrdpLogger;
import cz.vutbr.fit.openmrdp.messageprocessors.IdentifyMessageProcessor;
import cz.vutbr.fit.openmrdp.messageprocessors.LocateMessageProcessor;
import cz.vutbr.fit.openmrdp.messageprocessors.MessageProcessor;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageFactory;
import cz.vutbr.fit.openmrdp.messages.MessageProtocol;
import cz.vutbr.fit.openmrdp.messages.OperationType;
import cz.vutbr.fit.openmrdp.messages.address.Address;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyProdService;
import cz.vutbr.fit.openmrdp.security.AuthorizationLevel;
import cz.vutbr.fit.openmrdp.security.SecurityConstants;
import cz.vutbr.fit.openmrdp.security.UserAuthorizatorTestImpl;
import cz.vutbr.fit.openmrdp.server.AddressRetriever;
import cz.vutbr.fit.openmrdp.server.NonSecureServerHandler;
import cz.vutbr.fit.openmrdp.server.SecureServerHandler;
import cz.vutbr.fit.openmrdp.server.ServerConfiguration;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * The production implementation of the {@link OpenmRDPServerAPI}.
 *
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPServerAPIImpl implements OpenmRDPServerAPI {

    private final MessageService messageService;
    private final MessageProcessor locateMessageProcessor;
    private final MessageProcessor identifyMessageProcessor;
    private final InfoManager infoManager;
    private final Map<String, Integer> clientSequenceNumbers = new HashMap<>();
    private final ServerConfiguration serverConfiguration;
    private final Map<ClientEntry, BaseMessage> preparedMessages = new HashMap<>();
    private final MrdpLogger logger;

    /**
     * Public constructor of the OpenmRDP server API
     *
     * @param serverConfiguration - configuration parameters
     * @param logger - logger for logging of the errors
     */
    public OpenmRDPServerAPIImpl(ServerConfiguration serverConfiguration, MrdpLogger logger) {
        infoManager = InfoManager.getInfoManager(new InformationBaseTestService(), new OntologyProdService());
        messageService = new MessageService(new MessageSenderImpl(), new MessageReceiverImpl());
        locateMessageProcessor = new LocateMessageProcessor(infoManager);
        identifyMessageProcessor = new IdentifyMessageProcessor(infoManager);
        this.serverConfiguration = serverConfiguration;
        this.logger = logger;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void receiveMessages() {
        try {
            startHttpServer();
        } catch (NetworkCommunicationException e) {
            logger.logError(e.getMessage());
            return;
        }

        while (true) {
            BaseMessage receivedMessage;
            try {
                receivedMessage = messageService.receiveMessage();
            } catch (NetworkCommunicationException | MessageDeserializeException e) {
                logger.logError(e.getMessage());
                continue;
            }

            Address hostAddress;
            try {
                hostAddress = receivedMessage.getHostAddress();
            } catch (AddressSyntaxException e) {
                logger.logError(e.getMessage());
                continue;
            }

            removeOldEntries();

            String clientAddress = hostAddress.getHostAddress();
            logger.logDebug("received request from: " + clientAddress);

            Integer sequenceNumber = receivedMessage.getSequenceNumber();

            if (isMessageAlreadyProcessed(clientAddress, sequenceNumber)) {
                continue;
            }

            clientSequenceNumbers.put(clientAddress, sequenceNumber);

            BaseMessage responseMessage = null;
            try {
                if (receivedMessage.getOperationType().equals(OperationType.IDENTIFY)) {
                    responseMessage = identifyMessageProcessor.processMessage(receivedMessage);
                } else if (receivedMessage.getOperationType().equals(OperationType.LOCATE)) {
                    responseMessage = locateMessageProcessor.processMessage(receivedMessage);
                }
            } catch (AddressSyntaxException e) {
                logger.logError(e.getMessage());
                continue;
            }

            preparedMessages.put(new ClientEntry(clientAddress, sequenceNumber, Instant.now()), responseMessage);

            if (responseMessage != null
                    && responseMessage.getMessageBody() != null
                    && responseMessage.getMessageBody().getQuery() != null) {
                String message;
                try {
                    if (!serverConfiguration.getSecurityConfiguration().isSecureConnectionSupported()) {
                        message = MessageFactory.generateConnectionMessage(
                                serverConfiguration,
                                receivedMessage.getSequenceNumber() + 1,
                                MessageProtocol.HTTP.getName(),
                                AuthorizationLevel.NONE.getCode()
                        );
                    } else {
                        message = MessageFactory.generateConnectionMessage(
                                serverConfiguration,
                                receivedMessage.getSequenceNumber() + 1,
                                MessageProtocol.HTTPS.getName(),
                                AuthorizationLevel.REQUIRED.getCode()
                        );
                    }
                    messageService.sendInfoAboutConnection(hostAddress, message);
                } catch (IOException ioexc) {
                    logger.logError(ioexc.getMessage());
                }
            }
        }
    }

    private void startHttpServer() throws NetworkCommunicationException {
        try {
            if (serverConfiguration.getSecurityConfiguration().isSecureConnectionSupported()) {
                try {
                    startSecureServerListener();
                } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | CertificateException | KeyManagementException e) {
                    logger.logError("Exception during start of secure server listener");
                }
            } else {
                startNonSecureServerListener();
            }
        } catch (IOException e) {
            throw new NetworkCommunicationException("Attempt to start HTTP Server fails.", e);
        }
    }

    @Override
    public void addInformationToInformationBase(RDFTriple information) {
        infoManager.addInformationToBase(information);
    }

    @Override
    public void removeInformationFromInformationBase(RDFTriple information) {
        infoManager.removeInformationFromBase(information);
    }

    private boolean isMessageAlreadyProcessed(String clientAddress, Integer receivedSeqNum) {
        Integer sequenceNumber;
        sequenceNumber = clientSequenceNumbers.get(clientAddress);

        return sequenceNumber != null && receivedSeqNum <= sequenceNumber;
    }

    private void startSecureServerListener() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        InetSocketAddress address = new InetSocketAddress(AddressRetriever.getLocalIpAddress(),  27774);
        HttpsServer server = HttpsServer.create(address, 0);

        SSLContext sslContext = SSLContext.getInstance(SecurityConstants.SSL_CONTEXT);

        char[] password = "password".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream("examplekey.jks");
        ks.load(fis, password);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params){
                try{
                    SSLContext context = SSLContext.getDefault();
                    SSLEngine engine = context.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    SSLParameters defaultSSLParameters = context.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParameters);
                } catch (NoSuchAlgorithmException nsae){
                    logger.logError("Secure server start error.");
                }
            }

        });

        server.createContext("/auth", new SecureServerHandler(
                serverConfiguration.getSecurityConfiguration().getUserAuthorizator(),
                preparedMessages)
        );
        server.setExecutor(null);
        server.start();
    }

    private void startNonSecureServerListener() throws IOException {
        InetSocketAddress address = new InetSocketAddress(27774);
        HttpServer server = HttpServer.create(address, 0);

        server.createContext("/auth", new NonSecureServerHandler(preparedMessages));
        server.setExecutor(null);
        server.start();
    }

    private void removeOldEntries() {
        for (ClientEntry entry : preparedMessages.keySet()) {
            if (entry.getCreated().toEpochMilli() < Instant.now().minus(1, ChronoUnit.HOURS).toEpochMilli()) {
                preparedMessages.remove(entry);
            }
        }
    }
}
