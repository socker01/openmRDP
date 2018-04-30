package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.communication.*;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messages.*;
import cz.vutbr.fit.openmrdp.server.MessageType;

import java.io.*;
import java.net.*;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPClientApiImpl implements OpenmRDPClientAPI {

    private final MessageService messageService;
    private final String callbackURI;
    private long sequenceNumber = 0;

    public OpenmRDPClientApiImpl(String callbackURI) {
        messageService = new MessageService(new MessageSenderImpl(), new MessageReceiverImpl());
        this.callbackURI = callbackURI;
    }

    @Override
    public String locateResource(String resourceName) throws NetworkCommunicationException {
        BaseMessage locateMessage = MessageFactory.createLocateMessage(resourceName, callbackURI, getNextSequenceNumber());
        messageService.sendMRDPMessage(locateMessage);
        sequenceNumber++;

        try {
            String response = getResponseFromServer();
            MRDPServerResponseMessage responseMessage = MessageDeserializer.deserializeMRDPServerResponseMessage(response);

            URL url = new URL(responseMessage.getServerAddress() + "/auth");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            sequenceNumber++;
            connection.setRequestMethod(MessageType.GET.getCode());
            connection.setRequestProperty(HeaderType.USER_AGENT.getHeaderCode(), "Mozilla/5.0");
            connection.setRequestProperty(HeaderType.NSEQ.getHeaderCode(), String.valueOf(sequenceNumber));
            connection.setRequestProperty(HeaderType.CLIENT_ADDRESS.getHeaderCode(), callbackURI);

            connection.getResponseCode();

            String mRDPResponseRaw = getMRDPResponseFromServer(connection.getInputStream());

            return ReDELMessageParser.parseLocationFromRedelMessage(mRDPResponseRaw);
        } catch (IOException e) {
            //TODO: what here?
            e.printStackTrace();
        }

        return null;
    }

    private String getMRDPResponseFromServer(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private String getResponseFromServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(27741);
        Socket clientSocket = serverSocket.accept();

        String serverResponse = parseResponse(clientSocket);

        clientSocket.close();
        serverSocket.close();

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

    @Override
    public String identifyResource(String query) {
        return null;
    }

    private long getNextSequenceNumber() {
        if (sequenceNumber == MessageFactory.MAX_SEQUENCE_NUMBER - 1) {
            sequenceNumber = 0;
        }

        return ++sequenceNumber;
    }
}
