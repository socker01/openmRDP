package cz.vutbr.fit.openmrdp.processors;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageCreator;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.query.QueryResolver;

/**
 * @author Jiri Koudelka
 * @since 27.03.2018.
 */
public final class IdentifyMessageProcessor implements MessageProcessor {

    private final InfoManager infoManager;

    public IdentifyMessageProcessor(InfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @Override
    public BaseMessage processMessage(BaseMessage receivedMessage) throws AddressSyntaxException {

        QueryResolver queryResolver = new QueryResolver(infoManager);
        String foundedResource = queryResolver.resolveQuery(receivedMessage.getMessageBody(), receivedMessage.getResourceName());

        ReDELResponseDTO responseDTO;

        if (foundedResource == null) {
            responseDTO = createReDELResponseForNonExistingResourceName(receivedMessage);
        } else {
            responseDTO = createReDELResponseForFoundedResource(receivedMessage, foundedResource);
        }

        return MessageCreator.createReDELResponse(responseDTO);
    }

    private ReDELResponseDTO createReDELResponseForNonExistingResourceName(BaseMessage receivedMessage) throws AddressSyntaxException {
        ReDELResponseDTO responseDTO;
        responseDTO = new ReDELResponseDTO.Builder()
                .withAddress(receivedMessage.getHostAddress())
                .withSequenceNumber(receivedMessage.getSequenceNumber())
                .withResourceUri(null)
                .withResourceLocation(null)
                .build();

        return responseDTO;
    }

    private ReDELResponseDTO createReDELResponseForFoundedResource(BaseMessage receivedMessage, String foundedResource) throws AddressSyntaxException {
        ReDELResponseDTO responseDTO;
        String foundedResourceLocation = infoManager.findResourceLocation(foundedResource);

        responseDTO = new ReDELResponseDTO.Builder()
                .withAddress(receivedMessage.getHostAddress())
                .withSequenceNumber(receivedMessage.getSequenceNumber())
                .withResourceUri(foundedResource)
                .withResourceLocation(foundedResourceLocation)
                .build();

        return responseDTO;
    }
}
