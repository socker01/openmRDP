package cz.vutbr.fit.openmrdp.api;

import com.sun.net.httpserver.HttpServer;
import cz.vutbr.fit.openmrdp.cache.ClientEntry;
import cz.vutbr.fit.openmrdp.communication.MessageReceiverImpl;
import cz.vutbr.fit.openmrdp.communication.MessageSenderImpl;
import cz.vutbr.fit.openmrdp.communication.MessageService;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.MessageDeserializeException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messageprocessors.IdentifyMessageProcessor;
import cz.vutbr.fit.openmrdp.messageprocessors.LocateMessageProcessor;
import cz.vutbr.fit.openmrdp.messageprocessors.MessageProcessor;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.OperationType;
import cz.vutbr.fit.openmrdp.messages.address.Address;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyProdService;
import cz.vutbr.fit.openmrdp.security.SecurityConfiguration;
import cz.vutbr.fit.openmrdp.security.UserAuthorizatorTestImpl;
import cz.vutbr.fit.openmrdp.server.SecureServerHandler;
import cz.vutbr.fit.openmrdp.server.ServerConfiguration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPServerAPIImpl implements OpenmRDPServerAPI {

    private final MessageService messageService;
    private final MessageProcessor locateMessageProcessor;
    private final MessageProcessor identifyMessageProcessor;
    private final InfoManager infoManager;
    private final Map<String, Integer> clientSequenceNumbers = new HashMap<>();
    private final SecurityConfiguration securityConfiguration;
    private final ServerConfiguration serverConfiguration;
    private final Map<ClientEntry, BaseMessage> preparedMessages = new HashMap<>();

    public OpenmRDPServerAPIImpl(SecurityConfiguration securityConfiguration, ServerConfiguration serverConfiguration) {
        infoManager = InfoManager.getInfoManager(new InformationBaseTestService(), new OntologyProdService());
        messageService = new MessageService(new MessageSenderImpl(), new MessageReceiverImpl());
        locateMessageProcessor = new LocateMessageProcessor(infoManager);
        identifyMessageProcessor = new IdentifyMessageProcessor(infoManager);
        this.securityConfiguration = securityConfiguration;
        this.serverConfiguration = serverConfiguration;
    }

    @Override
    public void receiveMessages() {
        try {
            startNonSecureServerListener();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO what here?
        }

        while (true) {
            BaseMessage receivedMessage;
            try {
                receivedMessage = messageService.receiveMessage();
            } catch (NetworkCommunicationException | MessageDeserializeException e) {
                //TODO: maybe log or send error in HTTP?
                continue;
            }

            Address hostAddress;
            try {
                hostAddress = receivedMessage.getHostAddress();
            } catch (AddressSyntaxException e) {
                //TODO log exc
                continue;
            }

            String clientAddress = hostAddress.getHostAddress();

            Integer sequenceNumber = receivedMessage.getSequenceNumber();

            if (isMessageAlreadyProcessed(clientAddress, sequenceNumber)){
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
                    //TODO log?
                continue;
            }

            //TODO: start http server
            preparedMessages.put(new ClientEntry(clientAddress, sequenceNumber), responseMessage);

            if(responseMessage.getMessageBody() != null && responseMessage.getMessageBody().getQuery() != null){
                try {
                    if (securityConfiguration.isSupportSecureConnection()){
                        messageService.sendInfoAboutSecureConnection(hostAddress, serverConfiguration, receivedMessage.getSequenceNumber() + 1);
                    }else{
                        messageService.sendInfoAboutNonSecureConnection(hostAddress, serverConfiguration, receivedMessage.getSequenceNumber() + 1);
                    }
                } catch (IOException ioexc){
                    //TODO log
                }
            }
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

    private void startNonSecureServerListener() throws IOException {
        InetSocketAddress address = new InetSocketAddress(2774);
        HttpServer server = HttpServer.create(address, 0);

//        server.createContext("/auth", new NonSecureServerHandler(preparedMessages));
        server.createContext("/auth", new SecureServerHandler(new HashMap<>(), new UserAuthorizatorTestImpl(), preparedMessages));
        server.setExecutor(null);
        server.start();
    }
}
