package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.communication.MessageReceiverTestImpl;
import cz.vutbr.fit.openmrdp.communication.MessageSenderTestImpl;
import cz.vutbr.fit.openmrdp.communication.MessageService;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageFactory;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPApiImpl implements OpenmRDPClientApi {

    private final MessageService messageService;
    private final String callbackURI;

    public OpenmRDPApiImpl(String callbackURI) {
        //TODO: replace test services with prod services
        messageService = new MessageService(new MessageSenderTestImpl(), new MessageReceiverTestImpl());
        this.callbackURI = callbackURI;
    }

    @Override
    public String locateResource(String resourceName) throws NetworkCommunicationException {
        BaseMessage locateMessage = MessageFactory.createLocateMessage(resourceName, callbackURI);
        messageService.sendMRDPMessage(locateMessage);

        //TODO: implement ReDeL messages and receive redel message. Then return URL
        return "";
    }
}
