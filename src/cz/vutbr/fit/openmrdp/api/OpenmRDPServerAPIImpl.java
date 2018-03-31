package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.communication.MessageReceiverTestImpl;
import cz.vutbr.fit.openmrdp.communication.MessageSenderTestImpl;
import cz.vutbr.fit.openmrdp.communication.MessageService;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.OperationType;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.InformationBaseProdService;
import cz.vutbr.fit.openmrdp.processors.IdentifyMessageProcessor;
import cz.vutbr.fit.openmrdp.processors.LocateMessageProcessor;
import cz.vutbr.fit.openmrdp.processors.MessageProcessor;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPServerAPIImpl implements OpenmRDPServerAPI {

    private final MessageService messageService;
    private final MessageProcessor locateMessageProcessor;
    private final MessageProcessor identifyMessageProcessor;

    public OpenmRDPServerAPIImpl() {
        InfoManager infoManager = new InfoManager(new InformationBaseProdService());
        messageService = new MessageService(new MessageSenderTestImpl(), new MessageReceiverTestImpl());
        locateMessageProcessor = new LocateMessageProcessor(infoManager);
        identifyMessageProcessor = new IdentifyMessageProcessor(infoManager);
    }

    //TODO: address syntax exception is maybe internal error
    @Override
    public void receiveIncomingMessages() throws NetworkCommunicationException, AddressSyntaxException {
        //TODO: make infinite loop?
        BaseMessage receivedMessage = messageService.receiveMessage();

        BaseMessage responseMessage = null;
        if (receivedMessage.getOperationType().equals(OperationType.IDENTIFY)) {
            responseMessage = identifyMessageProcessor.processMessage(receivedMessage);
        } else if (receivedMessage.getOperationType().equals(OperationType.LOCATE)) {
            responseMessage = locateMessageProcessor.processMessage(receivedMessage);
        }

        messageService.sendReDELMessage(responseMessage);
    }

}
