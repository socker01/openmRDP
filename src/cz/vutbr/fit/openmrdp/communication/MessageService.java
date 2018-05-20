package cz.vutbr.fit.openmrdp.communication;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.address.Address;

import java.io.IOException;

/**
 * Service for message sending
 *
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class MessageService {

    private final MessageSender messageSender;
    private final MessageReceiver messageReceiver;

    public MessageService(@NotNull MessageSender messageSender, @NotNull MessageReceiver messageReceiver) {
        this.messageSender = Preconditions.checkNotNull(messageSender);
        this.messageReceiver = Preconditions.checkNotNull(messageReceiver);
    }

    public void sendMRDPMessage(@NotNull BaseMessage baseMessage) throws NetworkCommunicationException {
        try {
            messageSender.sendMRDPMessage(baseMessage);
        } catch (IOException e) {
            throw new NetworkCommunicationException(e.getMessage(), e.getCause());
        }
    }

    @NotNull
    public BaseMessage receiveMessage() throws NetworkCommunicationException {
        try {
            return messageReceiver.receiveMessages();
        } catch (IOException e) {
            throw new NetworkCommunicationException(e.getMessage(), e.getCause());
        }
    }

    public void sendInfoAboutConnection(@NotNull Address clientAddress, @NotNull String message) throws IOException {
        messageSender.sendInformationAboutConnection(clientAddress, message);
    }
}
