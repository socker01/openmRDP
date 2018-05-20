package cz.vutbr.fit.openmrdp.messageprocessors;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageFactory;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.Resource;
import cz.vutbr.fit.openmrdp.query.QueryResolver;

import java.util.Collections;
import java.util.List;

/**
 * Production implementation of {@link MessageProcessor} interface.
 * <p>
 * This processor is used for processing of the IDENTIFY messages.
 *
 * @author Jiri Koudelka
 * @since 27.03.2018.
 */
public final class IdentifyMessageProcessor implements MessageProcessor {

    @NotNull
    private final InfoManager infoManager;

    public IdentifyMessageProcessor(@NotNull InfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @Override
    @NotNull
    public BaseMessage processMessage(@NotNull BaseMessage receivedMessage) throws AddressSyntaxException {

        QueryResolver queryResolver = new QueryResolver(infoManager);
        List<String> foundResources = queryResolver.resolveQuery(receivedMessage.getMessageBody(), receivedMessage.getResourceName());

        ReDELResponseDTO responseDTO;

        if (foundResources.isEmpty()) {
            responseDTO = createReDELResponseForNonExistingResourceName(receivedMessage);
        } else {
            responseDTO = createReDELResponseForFoundResource(receivedMessage, foundResources.get(0));
        }

        return MessageFactory.createReDELResponse(responseDTO);
    }

    @NotNull
    private ReDELResponseDTO createReDELResponseForNonExistingResourceName(@NotNull BaseMessage receivedMessage) throws AddressSyntaxException {
        Resource resource = new Resource(null, null);

        return new ReDELResponseDTO.Builder()
                .withAddress(receivedMessage.getHostAddress())
                .withSequenceNumber(receivedMessage.getSequenceNumber())
                .withResource(Collections.singletonList(resource))
                .build();
    }

    @NotNull
    private ReDELResponseDTO createReDELResponseForFoundResource(@NotNull BaseMessage receivedMessage, @NotNull String foundResource)
            throws AddressSyntaxException {
        String foundResourceLocation = infoManager.findResourceLocation(foundResource);
        Resource resource = new Resource(foundResource, foundResourceLocation);

        return new ReDELResponseDTO.Builder()
                .withAddress(receivedMessage.getHostAddress())
                .withSequenceNumber(receivedMessage.getSequenceNumber())
                .withResource(Collections.singletonList(resource))
                .build();
    }
}
