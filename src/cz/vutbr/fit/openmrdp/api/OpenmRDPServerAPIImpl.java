package cz.vutbr.fit.openmrdp.api;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
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
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseProdService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyProdService;
import cz.vutbr.fit.openmrdp.security.AuthorizationLevel;
import cz.vutbr.fit.openmrdp.security.SecurityConstants;
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
     * @param logger              - logger for logging of the errors
     */
    public OpenmRDPServerAPIImpl(@NotNull ServerConfiguration serverConfiguration, @NotNull MrdpLogger logger) {
        infoManager = InfoManager.getInfoManager(new InformationBaseProdService(), new OntologyProdService());
        messageService = new MessageService(new MessageSenderImpl(), new MessageReceiverImpl());
        locateMessageProcessor = new LocateMessageProcessor(infoManager);
        identifyMessageProcessor = new IdentifyMessageProcessor(infoManager);
        this.serverConfiguration = Preconditions.checkNotNull(serverConfiguration, "Server configuration cannot be null");
        this.logger = Preconditions.checkNotNull(logger, "Logger cannot be null");
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
                    preparedMessages.remove(new ClientEntry(clientAddress, sequenceNumber, Instant.now()));
                    clientSequenceNumbers.remove(clientAddress);
                    logger.logError(ioexc.getMessage());
                }
            } else {
                clientSequenceNumbers.remove(clientAddress);
                preparedMessages.remove(new ClientEntry(clientAddress, sequenceNumber, Instant.now()));
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
    public void addInformationToInformationBase(@NotNull RDFTriple information) {
        Preconditions.checkNotNull(information, "Information to add cannot be null");
        infoManager.addInformationToBase(information);
    }

    @Override
    public void removeInformationFromInformationBase(@NotNull RDFTriple information) {
        Preconditions.checkNotNull(information, "Information to remove cannot be null");
        infoManager.removeInformationFromBase(information);
    }

    private boolean isMessageAlreadyProcessed(@NotNull String clientAddress, @NotNull Integer receivedSeqNum) {
        Integer sequenceNumber;
        sequenceNumber = clientSequenceNumbers.get(clientAddress);

        return sequenceNumber != null && receivedSeqNum <= sequenceNumber;
    }

    private void startSecureServerListener() throws IOException, NoSuchAlgorithmException, KeyStoreException,
            CertificateException, UnrecoverableKeyException, KeyManagementException {
        InetSocketAddress address = new InetSocketAddress(AddressRetriever.getLocalIpAddress(), serverConfiguration.getPort());
        HttpsServer server = HttpsServer.create(address, 0);

        SSLContext sslContext = SSLContext.getInstance(SecurityConstants.SSL_CONTEXT);

        char[] password = serverConfiguration.getSecurityConfiguration().getKeyStorePassword().toCharArray();
        KeyStore ks = KeyStore.getInstance(SecurityConstants.KEY_STORE);
        FileInputStream fis = new FileInputStream(serverConfiguration.getSecurityConfiguration().getKeyStorePath());
        ks.load(fis, password);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(SecurityConstants.SUN_X_509);
        kmf.init(ks, password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(SecurityConstants.SUN_X_509);
        tmf.init(ks);

        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                try {
                    SSLContext context = SSLContext.getDefault();
                    SSLEngine engine = context.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    SSLParameters defaultSSLParameters = context.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParameters);
                } catch (NoSuchAlgorithmException nsae) {
                    logger.logError("Secure server start error.");
                }
            }

        });

        server.createContext(MessageFactory.COMMUNICATION_ENDPOINT, new SecureServerHandler(
                serverConfiguration.getSecurityConfiguration().getUserAuthorizator(),
                preparedMessages, clientSequenceNumbers)
        );
        server.setExecutor(null);
        server.start();
    }

    private void startNonSecureServerListener() throws IOException {
        InetSocketAddress address = new InetSocketAddress(serverConfiguration.getPort());
        HttpServer server = HttpServer.create(address, 0);

        server.createContext(MessageFactory.COMMUNICATION_ENDPOINT, new NonSecureServerHandler(preparedMessages, clientSequenceNumbers));
        server.setExecutor(null);
        server.start();
    }

    private void removeOldEntries() {
        for (ClientEntry entry : preparedMessages.keySet()) {
            if (entry.getCreated().toEpochMilli() < Instant.now().minus(10, ChronoUnit.SECONDS).toEpochMilli()) {
                preparedMessages.remove(entry);
                clientSequenceNumbers.remove(entry.getAddress());
            }
        }
    }
}
