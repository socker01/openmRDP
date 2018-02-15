package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageSerializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
final class MessageSenderImpl implements MessageSender{

    @Override
    public void sendMessage(BaseMessage message) throws IOException {
        DatagramSocket udpSocket = new DatagramSocket();

        DatagramPacket packet = createPacket(message);

        for (int i = 0; i < 3; i++) {
            udpSocket.send(packet);
        }

        udpSocket.close();
    }

    private DatagramPacket createPacket(BaseMessage message) throws UnknownHostException {
        String rawMessage = MessageSerializer.serializeMessage(message);
        byte[] messageByteArray = rawMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(messageByteArray, messageByteArray.length);
        packet.setAddress(InetAddress.getByName(NetworkCommunicationConstants.BROADCAST_ADDRESS));
        packet.setPort(NetworkCommunicationConstants.PORT);

        return packet;
    }
}
