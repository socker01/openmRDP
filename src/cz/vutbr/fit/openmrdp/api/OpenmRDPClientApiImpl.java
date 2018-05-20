package cz.vutbr.fit.openmrdp.api;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.communication.CommunicationConfigurationConstants;
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
    private final MrdpLogger logger;
    private final boolean debugMode;
    private final int clientPort;

    private long sequenceNumber = 0;

    /**
     * Public constructor of the OpenmRDP client API
     *
     * @param callbackURI - URI where the client will be waiting for the response from the server
     * @param logger      - logger for logging of the errors
     */
    public OpenmRDPClientApiImpl(@NotNull String callbackURI, @NotNull MrdpLogger logger) {
        messageService = new MessageService(new MessageSenderImpl(), new MessageReceiverImpl());
        this.callbackURI = Preconditions.checkNotNull(callbackURI, "Callback URI cannot be null");
        this.logger = Preconditions.checkNotNull(logger, "Logger cannot be null");
        this.debugMode = false;

        Integer port = AddressParser.parsePort(callbackURI);
        this.clientPort = port != null ? port : CommunicationConfigurationConstants.DEFAULT_CONNECTION_INFORMATION_PORT;
    }

    /**
     * Public constructor of the OpenmRDP client API with possibility to set debugMode flag.
     * <p>
     * If the debug mode is set to true, the client accept also secure servers with self-signed certificates
     *
     * @param callbackURI - URI where the client will be waiting for the response from the server
     *                    * @param logger - logger for logging of the errors
     * @param debugMode   - debugMode flag. If the debugMode is true, client accept also self-signed certificates
     */
    public OpenmRDPClientApiImpl(@NotNull String callbackURI, @NotNull MrdpLogger logger, boolean debugMode) {
        messageService = new MessageService(new MessageSenderImpl(), new MessageReceiverImpl());
        this.callbackURI = Preconditions.checkNotNull(callbackURI, "Callback URI cannot be null");
        this.logger = Preconditions.checkNotNull(logger, "Logger cannot be null");
        this.debugMode = debugMode;

        Integer port = AddressParser.parsePort(callbackURI);
        this.clientPort = port != null ? port : CommunicationConfigurationConstants.DEFAULT_CONNECTION_INFORMATION_PORT;
    }

    @Override
    @Nullable
    public String locateResource(@NotNull String resourceName) throws NetworkCommunicationException {
        if (resourceName == null) {
            logger.logError("Resource name cannot be null");

            return null;
        }

        createAndSendLocateMessage(resourceName);

        try {
            ConnectionInformationMessage responseMessage = receiveConnectionInformation();

            URL url = createServerURL(responseMessage);

            HttpURLConnection connection = initializeConnection(url);

            connection.getResponseCode();
            String mRDPResponseRaw = getConnectionInformationString(connection.getInputStream());

            return ReDELMessageParser.parseLocationFromRedelMessage(mRDPResponseRaw);
        } catch (InterruptedIOException e) {
            return null;
        } catch (IOException e) {
            logger.logError("Message: " + e.getMessage());
            logger.logError("Body: " + e.toString());

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            return errors.toString();
        }
    }

    @Override
    @Nullable
    public String locateResource(@Nullable String resourceName, @Nullable String login, @Nullable String password)
            throws NetworkCommunicationException {

        if (isSomeParameterNull(resourceName, login, password)) {
            return null;
        }

        createAndSendLocateMessage(resourceName);
        try {
            ConnectionInformationMessage responseMessage = receiveConnectionInformation();

            URL url = createServerURL(responseMessage);

            HttpsURLConnection connection = initializeSecureConnection(url, login, password);

            connection.getResponseCode();
            String mRDPResponseRaw = getConnectionInformationString(connection.getInputStream());

            return ReDELMessageParser.parseLocationFromRedelMessage(mRDPResponseRaw);
        } catch (InterruptedIOException e) {
            return null;
        } catch (IOException e) {
            logger.logError("Message: " + e.getMessage());
            logger.logError("Body: " + e.toString());

            return e.getMessage();
        }
    }

    @Override
    @Nullable
    public String identifyResource(@NotNull String query) throws QuerySyntaxException, NetworkCommunicationException {
        if (query == null) {
            logger.logError("Identify query cannot be null");

            return null;
        }

        createAndSendIdentifyMessage(query);

        try {
            ConnectionInformationMessage responseMessage = receiveConnectionInformation();

            URL url = createServerURL(responseMessage);
            logger.logDebug(url.toString());

            HttpURLConnection connection = initializeConnection(url);

            connection.getResponseCode();

            return getConnectionInformationString(connection.getInputStream());
        } catch (InterruptedIOException e) {
            return null;
        } catch (IOException e) {
            logger.logError("Message: " + e.getMessage());
            logger.logError("Body: " + e.toString());

            return e.getMessage();
        }
    }

    @Override
    @Nullable
    public String identifyResource(@NotNull String query, @NotNull String login, @NotNull String password)
            throws QuerySyntaxException, NetworkCommunicationException {
        if (isSomeParameterNull(query, login, password)) {
            return null;
        }

        createAndSendIdentifyMessage(query);

        try {
            ConnectionInformationMessage responseMessage = receiveConnectionInformation();

            URL url = createServerURL(responseMessage);

            HttpsURLConnection connection = initializeSecureConnection(url, login, password);

            connection.getResponseCode();

            return getConnectionInformationString(connection.getInputStream());
        } catch (InterruptedIOException e) {
            return null;
        } catch (IOException e) {
            logger.logError("Message: " + e.getMessage());
            logger.logError("Body: " + e.toString());

            return e.getMessage();
        }
    }

    private boolean isSomeParameterNull(String resourceName, String login, String password) {
        if (resourceName == null) {
            logger.logError("Resource name cannot be null");

            return true;
        }

        if (login == null) {
            logger.logError("Login cannot be null");

            return true;
        }

        if (password == null) {
            logger.logError("Password cannot be null");

            return true;
        }

        return false;
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
            url = new URL(responseMessage.getMessageProtocol().getName().toLowerCase(), serverURL, port, endpoint);
        } else {
            url = new URL(responseMessage.getMessageProtocol().getName().toLowerCase(), serverURL, endpoint);
        }

        return url;
    }

    private HttpURLConnection initializeConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        sequenceNumber++;
        connection.setRequestMethod(MessageType.GET.getCode());
        connection.setRequestProperty(HeaderType.USER_AGENT.getHeaderCode(), CommunicationConfigurationConstants.USER_AGENT);
        connection.setRequestProperty(HeaderType.NSEQ.getHeaderCode(), String.valueOf(sequenceNumber));
        connection.setRequestProperty(HeaderType.CLIENT_ADDRESS.getHeaderCode(), callbackURI);
        connection.setRequestProperty(HeaderType.HOST.getHeaderCode(), AddressRetriever.getLocalIpAddress());

        return connection;
    }

    private HttpsURLConnection initializeSecureConnection(URL url, String login, String password) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        if (debugMode) {
            logger.logDebug("trust updated.");
            connection.setHostnameVerifier((hostname, session) -> true);
            try {
                ConnectionTrustVerifier.trustSelfSignedCertificates(connection);
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                logger.logError("Connection Trust modifier error: " + e.getMessage());
            }
        }

        sequenceNumber++;
        connection.setRequestMethod(MessageType.GET.getCode());
        connection.setRequestProperty(HeaderType.USER_AGENT.getHeaderCode(), CommunicationConfigurationConstants.USER_AGENT);
        connection.setRequestProperty(HeaderType.NSEQ.getHeaderCode(), String.valueOf(sequenceNumber));
        connection.setRequestProperty(HeaderType.CLIENT_ADDRESS.getHeaderCode(), callbackURI);
        connection.setRequestProperty(HeaderType.HOST.getHeaderCode(), AddressRetriever.getLocalIpAddress());
        connection.setRequestProperty(HeaderType.AUTHORIZATION.getHeaderCode(), encodeCredentials(login, password));

        return connection;
    }

    private String encodeCredentials(String login, String password) {
        try {
            return Base64.getEncoder().encodeToString((login + ":" + password).getBytes(CommunicationConfigurationConstants.DEFAULT_CHARSET));
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

    private long getNextSequenceNumber() {
        if (sequenceNumber == MessageFactory.MAX_SEQUENCE_NUMBER - 1) {
            sequenceNumber = 0;
        }

        return ++sequenceNumber;
    }
}
