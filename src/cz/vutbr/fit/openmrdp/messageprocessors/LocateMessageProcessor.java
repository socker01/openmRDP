package cz.vutbr.fit.openmrdp.messageprocessors;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageFactory;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.Resource;
import cz.vutbr.fit.openmrdp.server.AddressRetriever;

import java.net.SocketException;
import java.util.Collections;

/**
 * Production implementation of {@link MessageProcessor} interface.
 *
 * This processor is used for processing of the LOCATE messages.
 *
 * @author Jiri Koudelka
 * @since 09.03.2018.
 */
public final class LocateMessageProcessor implements MessageProcessor{

    @NotNull
    private final InfoManager infoManager;

    public LocateMessageProcessor(@NotNull InfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @Override
    @NotNull
    public BaseMessage processMessage(@NotNull BaseMessage locateMessage) throws AddressSyntaxException {
        String resourceLocation;
        try {
            resourceLocation = infoManager.findResourceLocation(locateMessage.getResourceName());
            if(resourceLocation != null){
                resourceLocation = AddressRetriever.getLocalIpAddress() + "/" + resourceLocation;
            }
        } catch (SocketException e) {
            resourceLocation = infoManager.findResourceLocation(locateMessage.getResourceName());
        }
        Resource resource = new Resource(locateMessage.getResourceName(), resourceLocation);

        ReDELResponseDTO responseDTO = new ReDELResponseDTO.Builder()
                .withAddress(locateMessage.getHostAddress())
                .withSequenceNumber(locateMessage.getSequenceNumber())
                .withResource(Collections.singletonList(resource))
                .build();

        return MessageFactory.createReDELResponse(responseDTO);
    }
}
