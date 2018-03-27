package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.communication.MessageReceiverTestImpl;
import cz.vutbr.fit.openmrdp.communication.MessageSenderTestImpl;
import cz.vutbr.fit.openmrdp.communication.MessageService;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageCreator;
import cz.vutbr.fit.openmrdp.messages.OperationType;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.InformationBaseProdService;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPServerAPIImpl implements OpenmRDPServerAPI {

    private final InfoManager infoManager;
    private final MessageService messageService;

    public OpenmRDPServerAPIImpl() {
        infoManager = new InfoManager(new InformationBaseProdService());
        messageService = new MessageService(new MessageSenderTestImpl(), new MessageReceiverTestImpl());
    }

    //TODO: address syntax exception is maybe internal error
    @Override
    public void receiveIncomingMessages() throws NetworkCommunicationException, AddressSyntaxException {
        //TODO: make infinite loop?
        BaseMessage receivedMessage = messageService.receiveMessage();

        if (receivedMessage.getOperationType().equals(OperationType.IDENTIFY)) {
            //TODO: implement me
        } else if (receivedMessage.getOperationType().equals(OperationType.LOCATE)) {
            String resourceLocation = infoManager.findResourceLocation(receivedMessage.getResourceName());

            ReDELResponseDTO responseDTO = new ReDELResponseDTO.Builder()
                    .withAddress(receivedMessage.getHostAddress())
                    .withSequenceNumber(receivedMessage.getSequenceNumber())
                    .withResourceLocation(resourceLocation)
                    .withResourceUri(receivedMessage.getResourceName())
                    .build();

            BaseMessage responseMessage = MessageCreator.createReDELResponse(responseDTO);

            messageService.sendReDELMessage(responseMessage);
        }
    }

    @Override
    public void addInformationToInformationModel(String subject, String predicate, String object) {
        infoManager.addInformationToBase(subject, predicate, object);
    }
}
