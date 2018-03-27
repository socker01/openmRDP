package cz.vutbr.fit.openmrdp.model.processor;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageCreator;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.model.InfoManager;

/**
 * @author Jiri Koudelka
 * @since 09.03.2018.
 */
public final class LocateMessageProcessor implements MessageProcessor{

    private final InfoManager infoManager;

    public LocateMessageProcessor(InfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @Override
    public BaseMessage processMessage(BaseMessage locateMessage) throws AddressSyntaxException {
        String resourceLocation = infoManager.findResourceLocation(locateMessage.getResourceName());

        ReDELResponseDTO responseDTO = new ReDELResponseDTO.Builder()
                .withAddress(locateMessage.getHostAddress())
                .withSequenceNumber(locateMessage.getSequenceNumber())
                .withResourceLocation(resourceLocation)
                .withResourceUri(locateMessage.getResourceName())
                .build();

        return MessageCreator.createReDELResponse(responseDTO);
    }
}
