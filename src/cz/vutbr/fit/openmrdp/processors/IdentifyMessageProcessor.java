package cz.vutbr.fit.openmrdp.processors;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageCreator;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.Resource;
import cz.vutbr.fit.openmrdp.query.QueryResolver;

import java.util.Collections;
import java.util.List;

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
        List<String> foundedResources = queryResolver.resolveQuery(receivedMessage.getMessageBody(), receivedMessage.getResourceName());

        ReDELResponseDTO responseDTO;

        if (foundedResources.isEmpty()) {
            responseDTO = createReDELResponseForNonExistingResourceName(receivedMessage);
        } else {
            responseDTO = createReDELResponseForFoundedResource(receivedMessage, foundedResources.get(0));
        }

        return MessageCreator.createReDELResponse(responseDTO);
    }

    private ReDELResponseDTO createReDELResponseForNonExistingResourceName(BaseMessage receivedMessage) throws AddressSyntaxException {
        Resource resource = new Resource(null, null);

        return new ReDELResponseDTO.Builder()
                .withAddress(receivedMessage.getHostAddress())
                .withSequenceNumber(receivedMessage.getSequenceNumber())
                .withResource(Collections.singletonList(resource))
                .build();
    }

    private ReDELResponseDTO createReDELResponseForFoundedResource(BaseMessage receivedMessage, String foundedResource) throws AddressSyntaxException {
        String foundedResourceLocation = infoManager.findResourceLocation(foundedResource);
        //TODO: resourceLocation null is valid state
        Resource resource = new Resource(foundedResource, foundedResourceLocation);

        return new ReDELResponseDTO.Builder()
                .withAddress(receivedMessage.getHostAddress())
                .withSequenceNumber(receivedMessage.getSequenceNumber())
                .withResource(Collections.singletonList(resource))
                .build();
    }
}
