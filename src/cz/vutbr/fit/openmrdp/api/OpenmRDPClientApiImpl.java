package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.communication.MessageReceiverImpl;
import cz.vutbr.fit.openmrdp.communication.MessageSenderImpl;
import cz.vutbr.fit.openmrdp.communication.MessageService;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.logger.MrdpLogger;
import cz.vutbr.fit.openmrdp.messages.*;
import cz.vutbr.fit.openmrdp.server.MessageType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPClientApiImpl implements OpenmRDPClientAPI {

    private final MessageService messageService;
    private final String callbackURI;
    private long sequenceNumber = 0;

    private final MrdpLogger logger;

    public OpenmRDPClientApiImpl(String callbackURI, MrdpLogger logger) {
        messageService = new MessageService(new MessageSenderImpl(), new MessageReceiverImpl());
        this.callbackURI = callbackURI;
        this.logger = logger;
    }

    @Override
    public String locateResource(String resourceName) throws NetworkCommunicationException {
        BaseMessage locateMessage = MessageFactory.createLocateMessage(resourceName, callbackURI, getNextSequenceNumber());
        messageService.sendMRDPMessage(locateMessage);
        sequenceNumber++;

        try {
            String response = getResponseFromServer(logger);
            MRDPServerResponseMessage responseMessage = MessageDeserializer.deserializeMRDPServerResponseMessage(response);

            HttpURLConnection connection;
            try {
//                URL url = new URL("http://" + responseMessage.getServerAddress() + "/auth");
                URL url = new URL("http",   "192.168.1.53", 2774, "/auth");
                logger.logDebug("url: " + url.toString());
                connection = (HttpURLConnection) url.openConnection();
            }catch (NullPointerException e){
                return "fuu1u124";
            }

            logger.logDebug("connection created." + responseMessage.getServerAddress());
            sequenceNumber++;
            connection.setRequestMethod(MessageType.GET.getCode());
            connection.setRequestProperty(HeaderType.USER_AGENT.getHeaderCode(), "Mozilla/5.0");
            connection.setRequestProperty(HeaderType.NSEQ.getHeaderCode(), String.valueOf(sequenceNumber));
            connection.setRequestProperty(HeaderType.CLIENT_ADDRESS.getHeaderCode(), callbackURI);
            connection.setRequestProperty("Host", "192.168.1.53");

            logger.logDebug("headers setted" + responseMessage.getServerAddress());
            try{
                connection.getResponseCode();
            } catch (NullPointerException e){
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                return sw.toString();
            }

            logger.logDebug("response code." + responseMessage.getServerAddress());

            String mRDPResponseRaw;
            try {
                mRDPResponseRaw = getMRDPResponseFromServer(connection.getInputStream());
            }
            catch (NullPointerException e){
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                return sw.toString();
            }
            logger.logDebug("get data from message");

            try {
                return ReDELMessageParser.parseLocationFromRedelMessage(mRDPResponseRaw);
            } catch (NullPointerException e){
                return "fuuu";
            }
        } catch (InterruptedIOException e) {
            return "No info about resource: " + resourceName;
        } catch (IOException e) {
            logger.logError("e1" + e.getMessage());
            logger.logError("e2" + e.toString());
            return  e.getMessage();
        }
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

    private String getResponseFromServer(MrdpLogger logger) throws IOException {
        ServerSocket serverSocket = new ServerSocket(27741);
        logger.logDebug("socket created");
        serverSocket.setSoTimeout(10000);
        Socket clientSocket;
        try {
            clientSocket = serverSocket.accept();
        }catch (InterruptedIOException exc){
            logger.logError("No response from servers.");
            throw exc;
        }
        logger.logDebug("socket accept");

        String serverResponse = parseResponse(clientSocket);
        logger.logDebug("response parsed");

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
