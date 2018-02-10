package cz.vutbr.fit.openmrdp.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
public final class MessageReceiver {

    public static void receiveMessages() throws IOException {
        InetAddress ipAddress = InetAddress.getByName(NetworkCommunicationConstants.BROADCAST_ADDRESS);
        MulticastSocket socket = createAndConfigureSocket(ipAddress);

        DatagramPacket packet = new DatagramPacket(new byte[NetworkCommunicationConstants.MAX_UDP_DATAGRAM_SIZE], NetworkCommunicationConstants.MAX_UDP_DATAGRAM_SIZE);

        //TODO: make the receiving of socket better
        socket.receive(packet);

        closeSocket(ipAddress, socket);
    }

    private static void closeSocket(InetAddress ipAddress, MulticastSocket socket) throws IOException {
        socket.leaveGroup(ipAddress);
        socket.close();
    }

    private static MulticastSocket createAndConfigureSocket(InetAddress ipAddress) throws IOException {
        MulticastSocket socket = new MulticastSocket(NetworkCommunicationConstants.PORT);
        socket.joinGroup(ipAddress);
        return socket;
    }
}
