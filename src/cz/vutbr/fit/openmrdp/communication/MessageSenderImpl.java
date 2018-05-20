package cz.vutbr.fit.openmrdp.communication;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageSerializer;
import cz.vutbr.fit.openmrdp.messages.address.Address;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

/**
 * The production implementation of the {@link MessageSender} interface
 *
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
public final class MessageSenderImpl implements MessageSender {

    @Override
    public void sendMRDPMessage(@NotNull BaseMessage message) throws IOException {
        DatagramSocket udpSocket = new DatagramSocket();

        DatagramPacket packet = createMRDPPacket(message);

        for (int i = 0; i < 3; i++) {
            udpSocket.send(packet);
        }

        udpSocket.close();
    }

    @Override
    public void sendInformationAboutConnection(@NotNull Address clientAddress, @NotNull String message) throws IOException {
        String hostAddress = clientAddress.getHostAddress();

        int delimiterIndex = hostAddress.indexOf(":");

        String ipAddress = hostAddress.substring(0, delimiterIndex);
        int port = Integer.parseInt(hostAddress.substring(delimiterIndex + 1));
        Socket socket = new Socket(ipAddress, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println(message);

        out.close();
        socket.close();
    }

    @NotNull
    private DatagramPacket createMRDPPacket(@NotNull BaseMessage message) throws UnknownHostException {
        String rawMessage = MessageSerializer.serializeMessage(message);
        byte[] messageByteArray = rawMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(messageByteArray, messageByteArray.length);
        packet.setAddress(InetAddress.getByName(NetworkCommunicationConstants.MULTICAST_ADDRESS));
        packet.setPort(NetworkCommunicationConstants.PORT);

        return packet;
    }
}
