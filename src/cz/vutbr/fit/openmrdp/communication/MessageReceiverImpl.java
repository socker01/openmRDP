package cz.vutbr.fit.openmrdp.communication;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageDeserializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * The production implementation of the {@link MessageReceiver}
 *
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
public final class MessageReceiverImpl implements MessageReceiver {

    @Override
    @NotNull
    public BaseMessage receiveMessages() throws IOException {
        InetAddress ipAddress = InetAddress.getByName(NetworkCommunicationConstants.MULTICAST_ADDRESS);
        MulticastSocket socket = createAndConfigureSocket(ipAddress);

        DatagramPacket packet = new DatagramPacket(new byte[NetworkCommunicationConstants.MAX_UDP_DATAGRAM_SIZE], NetworkCommunicationConstants.MAX_UDP_DATAGRAM_SIZE);

        socket.receive(packet);

        BaseMessage receivedMessage = MessageDeserializer.deserializeMessage(new String(packet.getData()));
        closeSocket(ipAddress, socket);

        return receivedMessage;
    }

    private void closeSocket(@NotNull InetAddress ipAddress, @NotNull MulticastSocket socket) throws IOException {
        socket.leaveGroup(ipAddress);
        socket.close();
    }

    @NotNull
    private static MulticastSocket createAndConfigureSocket(@NotNull InetAddress ipAddress) throws IOException {
        MulticastSocket socket = new MulticastSocket(NetworkCommunicationConstants.PORT);
        socket.joinGroup(ipAddress);

        return socket;
    }
}
