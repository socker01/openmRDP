package cz.vutbr.fit.openmrdp.messageprocessors;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageFactory;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.Resource;
import cz.vutbr.fit.openmrdp.server.ServerAddressRetriever;

import java.net.SocketException;
import java.util.Collections;

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
        String resourceLocation;
        try {
            resourceLocation = ServerAddressRetriever.getLocalIpAddress() + "/" + infoManager.findResourceLocation(locateMessage.getResourceName());
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
