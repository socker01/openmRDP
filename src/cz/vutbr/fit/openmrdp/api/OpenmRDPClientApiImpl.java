package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.communication.MessageReceiverImpl;
import cz.vutbr.fit.openmrdp.communication.MessageSenderImpl;
import cz.vutbr.fit.openmrdp.communication.MessageService;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.logger.MrdpLogger;
import cz.vutbr.fit.openmrdp.messages.*;
import cz.vutbr.fit.openmrdp.messages.address.AddressParser;
import cz.vutbr.fit.openmrdp.query.QueryParser;
import cz.vutbr.fit.openmrdp.query.QueryRaw;
import cz.vutbr.fit.openmrdp.security.ConnectionTrustVerifier;
import cz.vutbr.fit.openmrdp.server.AddressRetriever;
import cz.vutbr.fit.openmrdp.server.MessageType;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * The production implementation of the {@link OpenmRDPClientAPI}.
 *
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPClientApiImpl implements OpenmRDPClientAPI {

    private final MessageService messageService;
    private final String callbackURI;
    private long sequenceNumber = 0;

    private final MrdpLogger logger;

    /**
     * Public constructor of the OpenmRDP client API
     *
     * @param callbackURI - URI where the client will be waiting for the response from the server
     * @param logger - logger for logging of the errors
     */
    public OpenmRDPClientApiImpl(String callbackURI, MrdpLogger logger) {
        messageService = new MessageService(new MessageSenderImpl(), new MessageReceiverImpl());
        this.callbackURI = callbackURI;
        this.logger = logger;
    }

    @Override
    public String locateResource(String resourceName) throws NetworkCommunicationException {
        createAndSendLocateMessage(resourceName);

        try {
            ConnectionInformationMessage responseMessage = receiveConnectionInformation();

            URL url = createServerURL(responseMessage);

            HttpURLConnection connection = initializeConnection(url);

            connection.getResponseCode();
            String mRDPResponseRaw = getConnectionInformationString(connection.getInputStream());

            return ReDELMessageParser.parseLocationFromRedelMessage(mRDPResponseRaw);
        } catch (InterruptedIOException e) {
            return "No info about resource: " + resourceName;
        } catch (IOException e) {
            logger.logError("Message: " + e.getMessage());
            logger.logError("Body: " + e.toString());

            return e.getMessage();
        }
    }

    @Override
    public String locateResource(String resourceName, String login, String password) throws NetworkCommunicationException {
        createAndSendLocateMessage(resourceName);
        try {
            ConnectionInformationMessage responseMessage = receiveConnectionInformation();

            URL url = createServerURL(responseMessage);

            HttpsURLConnection connection = initializeSecureConnection(url, login, password);

            connection.getResponseCode();
            String mRDPResponseRaw = getConnectionInformationString(connection.getInputStream());

            return ReDELMessageParser.parseLocationFromRedelMessage(mRDPResponseRaw);
        } catch (InterruptedIOException e) {
            return "No info about resource: " + resourceName;
        } catch (IOException e) {
            logger.logError("Message: " + e.getMessage());
            logger.logError("Body: " + e.toString());
            return e.getMessage();
        }
    }

    @Override
    public String identifyResource(String query) throws QuerySyntaxException, NetworkCommunicationException {
        createAndSendIdentifyMessage(query);

        try {
            ConnectionInformationMessage responseMessage = receiveConnectionInformation();

            URL url = createServerURL(responseMessage);
            logger.logDebug(url.toString());

            HttpURLConnection connection = initializeConnection(url);

            connection.getResponseCode();

            return getConnectionInformationString(connection.getInputStream());
        } catch (InterruptedIOException e) {
            return "No result for query: " + query;
        } catch (IOException e) {
            logger.logError("Message: " + e.getMessage());
            logger.logError("Body: " + e.toString());

            return e.getMessage();
        }
    }

    @Override
    public String identifyResource(String query, String login, String password) throws QuerySyntaxException, NetworkCommunicationException {
        createAndSendIdentifyMessage(query);

        try {
            ConnectionInformationMessage responseMessage = receiveConnectionInformation();

            URL url = createServerURL(responseMessage);

            HttpsURLConnection connection = initializeSecureConnection(url, login, password);

            connection.getResponseCode();

            return getConnectionInformationString(connection.getInputStream());
        } catch (InterruptedIOException e) {
            return "No result for query: " + query;
        } catch (IOException e) {
            logger.logError("Message: " + e.getMessage());
            logger.logError("Body: " + e.toString());
            return e.getMessage();
        }
    }

    private void createAndSendLocateMessage(String resourceName) throws NetworkCommunicationException {
        BaseMessage locateMessage = MessageFactory.createLocateMessage(resourceName, callbackURI, getNextSequenceNumber());
        messageService.sendMRDPMessage(locateMessage);
        sequenceNumber++;
    }

    private void createAndSendIdentifyMessage(String query) throws QuerySyntaxException, NetworkCommunicationException {
        QueryRaw queryRaw = QueryParser.parseQuery(query);
        MessageBody messageBody = new MessageBody(queryRaw.getConditions(), ContentType.PLANT_QUERY);
        logger.logDebug(queryRaw.getConditions());
        BaseMessage identifyMessage = MessageFactory.createIdentifyMessage(
                queryRaw.getResourceName(),
                callbackURI,
                messageBody,
                getNextSequenceNumber()
        );

        messageService.sendMRDPMessage(identifyMessage);
        sequenceNumber++;
    }

    private ConnectionInformationMessage receiveConnectionInformation() throws IOException {
        String response = getResponseFromServer();

        return MessageDeserializer.deserializeMRDPServerResponseMessage(response);
    }

    private URL createServerURL(ConnectionInformationMessage responseMessage) throws MalformedURLException {
        String serverAddress = responseMessage.getServerAddress();

        String serverURL = AddressParser.parseAddressWithoutPort(serverAddress);
        Integer port = AddressParser.parsePort(serverAddress);
        String endpoint = AddressParser.parseEndpoint(serverAddress);

        URL url;
        if (port != null) {
            url = new URL(responseMessage.getMessageProtocol().getName(), serverURL, port, endpoint);
        } else {
            url = new URL(responseMessage.getMessageProtocol().getName(), serverURL, endpoint);
        }

        return url;
    }

    private HttpURLConnection initializeConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        sequenceNumber++;
        connection.setRequestMethod(MessageType.GET.getCode());
        connection.setRequestProperty(HeaderType.USER_AGENT.getHeaderCode(), "Mozilla/5.0");
        connection.setRequestProperty(HeaderType.NSEQ.getHeaderCode(), String.valueOf(sequenceNumber));
        connection.setRequestProperty(HeaderType.CLIENT_ADDRESS.getHeaderCode(), callbackURI);
        connection.setRequestProperty(HeaderType.HOST.getHeaderCode(), AddressRetriever.getLocalIpAddress());

        return connection;
    }

    private HttpsURLConnection initializeSecureConnection(URL url, String login, String password) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setHostnameVerifier((hostname, session) -> true);
        try {
            ConnectionTrustVerifier.trustSelfSignedCertificates(connection);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            logger.logError("Trust modifier error.");
        }

        sequenceNumber++;
        connection.setRequestMethod(MessageType.GET.getCode());
        connection.setRequestProperty(HeaderType.USER_AGENT.getHeaderCode(), "Mozilla/5.0");
        connection.setRequestProperty(HeaderType.NSEQ.getHeaderCode(), String.valueOf(sequenceNumber));
        connection.setRequestProperty(HeaderType.CLIENT_ADDRESS.getHeaderCode(), callbackURI);
        connection.setRequestProperty(HeaderType.HOST.getHeaderCode(), AddressRetriever.getLocalIpAddress());
        connection.setRequestProperty(HeaderType.AUTHORIZATION.getHeaderCode(), encodeCredentials(login, password));

        return connection;
    }

    private String encodeCredentials(String login, String password) {
        try {
            return Base64.getEncoder().encodeToString((login + ":" + password).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.logError(e.getMessage());
        }

        return null;
    }

    private String getConnectionInformationString(InputStream inputStream) throws IOException {
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
        serverSocket.setSoTimeout(10000);
        Socket clientSocket = null;
        String serverResponse;
        try {
            clientSocket = serverSocket.accept();
            serverResponse = parseResponse(clientSocket);
        } catch (InterruptedIOException exc) {
            logger.logInfo("No response from servers.");
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

    private long getNextSequenceNumber() {
        if (sequenceNumber == MessageFactory.MAX_SEQUENCE_NUMBER - 1) {
            sequenceNumber = 0;
        }

        return ++sequenceNumber;
    }
}
