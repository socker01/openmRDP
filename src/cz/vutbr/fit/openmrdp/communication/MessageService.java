package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.address.Address;
import cz.vutbr.fit.openmrdp.server.ServerConfiguration;

import java.io.IOException;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class MessageService {

    private final MessageSender messageSender;
    private final MessageReceiver messageReceiver;

    public MessageService(MessageSender messageSender, MessageReceiver messageReceiver) {
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
    }

    public void sendMRDPMessage(BaseMessage baseMessage) throws NetworkCommunicationException {
        try {
            messageSender.sendMRDPMessage(baseMessage);
        } catch (IOException e) {
            throw new NetworkCommunicationException(e.getMessage(), e.getCause());
        }
    }

    public void sendReDELMessage(BaseMessage baseMessage) throws IOException {
        messageSender.sendReDELMessage(baseMessage);
    }

    public BaseMessage receiveMessage() throws NetworkCommunicationException {
        try {
            return messageReceiver.receiveMessages();
        } catch (IOException e) {
            throw new NetworkCommunicationException(e.getMessage(), e.getCause());
        }
    }

    public void sendInfoAboutSecureConnection(Address clientAddress, ServerConfiguration serverConfiguration, int sequenceNumber) throws IOException {
        messageSender.sendInformationAboutSecureConnection(clientAddress, serverConfiguration, sequenceNumber);
    }

    public void sendInfoAboutNonSecureConnection(Address clientAddress, ServerConfiguration serverConfiguration, int sequenceNumber) throws IOException {
        messageSender.sendInformationAboutNonSecureConnection(clientAddress, serverConfiguration, sequenceNumber);
    }
}
