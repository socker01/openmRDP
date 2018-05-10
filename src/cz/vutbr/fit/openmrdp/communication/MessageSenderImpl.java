package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageSerializer;
import cz.vutbr.fit.openmrdp.messages.address.Address;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

/**
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
public final class MessageSenderImpl implements MessageSender{

    @Override
    public void sendMRDPMessage(BaseMessage message) throws IOException {
        DatagramSocket udpSocket = new DatagramSocket();

        DatagramPacket packet = createMRDPPacket(message);

        for (int i = 0; i < 3; i++) {
            udpSocket.send(packet);
        }

        udpSocket.close();
    }

    @Override
    public void sendReDELMessage(BaseMessage message) throws IOException {
        URL url;
        try {
            String rawUrl = message.getHostAddress().getCompleteAddress();
            url = new URL(rawUrl);
        } catch (AddressSyntaxException e) {
            throw new MalformedURLException(e.getMessage());
        }

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        connection.setDoOutput(true);
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.writeBytes(message.getMessageBody().getQuery());
        writer.flush();
        writer.close();
    }

    @Override
    public void sendInformationAboutConnection(Address clientAddress, String message) throws IOException {
        String hostAddress = clientAddress.getHostAddress();

        int delimiterIndex = hostAddress.indexOf(":");

        String ipAddress = hostAddress.substring(0, delimiterIndex);
        int port = Integer.parseInt(hostAddress.substring(delimiterIndex+1));
        System.out.println("Send response to :" + ipAddress + " : port :" + port);
        Socket socket = new Socket(ipAddress, port);
        System.out.println("socket created");
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println(message);

        System.out.println("message seded");
        out.close();
        socket.close();
    }

    private DatagramPacket createMRDPPacket(BaseMessage message) throws UnknownHostException {
        String rawMessage = MessageSerializer.serializeMessage(message);
        byte[] messageByteArray = rawMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(messageByteArray, messageByteArray.length);
        packet.setAddress(InetAddress.getByName(NetworkCommunicationConstants.MULTICAST_ADDRESS));
        packet.setPort(NetworkCommunicationConstants.PORT);

        return packet;
    }
}
