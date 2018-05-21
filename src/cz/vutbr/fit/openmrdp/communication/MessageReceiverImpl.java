package cz.vutbr.fit.openmrdp.communication;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.logger.MrdpLogger;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.ConnectionInformationMessage;
import cz.vutbr.fit.openmrdp.messages.MessageDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.*;

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

    @Override
    public ConnectionInformationMessage receiveConnectionInformationMessage(int clientPort, MrdpLogger logger) throws IOException {
        String response = getResponseFromServer(clientPort, logger);

        return MessageDeserializer.deserializeMRDPServerResponseMessage(response);
    }

    private String getResponseFromServer(int clientPort, MrdpLogger logger) throws IOException {
        ServerSocket serverSocket = new ServerSocket(clientPort);
        serverSocket.setSoTimeout(CommunicationConfigurationConstants.DEFAULT_SOCKET_TIMEOUT);
        Socket clientSocket = null;
        String serverResponse;
        try {
            clientSocket = serverSocket.accept();
            serverResponse = parseResponse(clientSocket);
        } catch (InterruptedIOException exc) {
            logger.logInfo("No response from servers");
            throw exc;
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
            serverSocket.close();
        }

        return serverResponse;
    }

    private String parseResponse(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        StringBuilder fromClient = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            fromClient.append("\n");
            fromClient.append(line);
        }

        in.close();

        return fromClient.toString();
    }
}
