package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.communication.*;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageFactory;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPClientApiImpl implements OpenmRDPClientAPI {

    private final MessageService messageService;
    private final String callbackURI;

    public OpenmRDPClientApiImpl(String callbackURI) {
        messageService = new MessageService(new MessageSenderImpl(), new MessageReceiverImpl());
        this.callbackURI = callbackURI;
    }

    @Override
    public String locateResource(String resourceName) throws NetworkCommunicationException {
        BaseMessage locateMessage = MessageFactory.createLocateMessage(resourceName, callbackURI);
        messageService.sendMRDPMessage(locateMessage);

        //TODO: implement ReDeL messages and receive redel message. Then return URL
        return "";
    }

    @Override
    public String identifyResource(String query) {
        return null;
    }
}
