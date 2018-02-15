package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;

import java.io.IOException;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class MessageService {

    private MessageSender messageSender = new MessageSenderImpl();
    private MessageReceiver messageReceiver = new MessageReceiverImpl();

    public void sendMRDPMessage(BaseMessage baseMessage) throws NetworkCommunicationException {
        try {
            messageSender.sendMessage(baseMessage);
        } catch (IOException e) {
            throw new NetworkCommunicationException(e.getMessage(), e.getCause());
        }
    }

    public BaseMessage receiveMessage() throws NetworkCommunicationException {
        try {
            return messageReceiver.receiveMessages();
        } catch (IOException e) {
            throw new NetworkCommunicationException(e.getMessage(), e.getCause());
        }
    }
}
