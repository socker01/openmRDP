package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.server.ServerConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating of all message types.
 *
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public final class MessageFactory {

    public static final long MAX_SEQUENCE_NUMBER = 2147483648L;    //TCP standard - 2^31
    public static final String COMMUNICATION_ENDPOINT = "/auth";

    private static final String SEQUENCE_NUMBER_TAG = "NSEQ";

    static final String SERVER_TAG = "SERVER";
    static final String PROTOCOL_TAG = "PROTOCOL";
    static final String AUTHORIZATION_TAG = "AUTHORIZATION";

    /**
     * Create corresponding LOCATE message
     *
     * @param resourceName   - name of the resource to locate
     * @param callbackURI    - client callback URI
     * @param sequenceNumber - communication sequence number
     * @return - {@link BaseMessage}
     */
    @NotNull
    public static BaseMessage createLocateMessage(@NotNull String resourceName, @NotNull String callbackURI, long sequenceNumber) {
        OperationLine operationLine = new OperationLine(OperationType.LOCATE, resourceName, MessageProtocol.MRDP);

        return new BaseMessage(operationLine, createBasicMessageHeaders(callbackURI, sequenceNumber), null);
    }

    @NotNull
    private static Map<HeaderType, String> createBasicMessageHeaders(@NotNull String callbackURI, long sequenceNumber) {
        Map<HeaderType, String> headers = new HashMap<>();

        headers.put(HeaderType.NSEQ, String.valueOf(sequenceNumber));
        headers.put(HeaderType.CALLBACK_URI, callbackURI);

        return headers;
    }

    /**
     * Create corresponding IDENTIFY message
     *
     * @param resourceName   - the name of the variable to identify
     * @param callbackURI    - client callback URI
     * @param body           - body of the IDENTIFY message
     * @param sequenceNumber - communication sequence number
     * @return - {@link BaseMessage}
     */
    @NotNull
    public static BaseMessage createIdentifyMessage(@NotNull String resourceName, @NotNull String callbackURI, @NotNull MessageBody body, long sequenceNumber) {
        OperationLine operationLine = new OperationLine(OperationType.IDENTIFY, resourceName, MessageProtocol.MRDP);

        Map<HeaderType, String> headers = createLocateMessageHeaders(callbackURI, body, sequenceNumber);

        return new BaseMessage(operationLine, headers, body);
    }

    @NotNull
    private static Map<HeaderType, String> createLocateMessageHeaders(@NotNull String callbackURI, @NotNull MessageBody body, long sequenceNumber) {
        Map<HeaderType, String> headers = createBasicMessageHeaders(callbackURI, sequenceNumber);

        headers.put(HeaderType.CONTENT_TYPE, body.getContentType().getCode());
        headers.put(HeaderType.CONTENT_LENGTH, String.valueOf(body.calculateBodyLength()));

        return headers;
    }

    /**
     * Create corresponding ReDEL message
     *
     * @param responseDTO - DTO with data about ReDEL message
     * @return - {@link BaseMessage}
     */
    @NotNull
    public static BaseMessage createReDELResponse(@NotNull ReDELResponseDTO responseDTO) {
        OperationLine operationLine = new OperationLine(OperationType.POST, responseDTO.getAddress().getEndPoint(), MessageProtocol.HTTP);

        MessageBody messageBody = ReDELMessageBodyFactory.createRedelMessage(responseDTO.getResources());

        Map<HeaderType, String> headers = createReDELMessageHeaders(responseDTO, messageBody);

        return new BaseMessage(operationLine, headers, messageBody);
    }

    @NotNull
    private static Map<HeaderType, String> createReDELMessageHeaders(@NotNull ReDELResponseDTO responseDTO, @NotNull MessageBody messageBody) {
        Map<HeaderType, String> headers = new HashMap<>();

        headers.put(HeaderType.HOST, responseDTO.getAddress().getHostAddress());
        headers.put(HeaderType.NSEQ, String.valueOf(responseDTO.getSequenceNumber()));
        headers.put(HeaderType.CONTENT_TYPE, messageBody.getContentType().getCode());
        headers.put(HeaderType.CONTENT_LENGTH, messageBody.getQuery() == null ? String.valueOf(0) : String.valueOf(messageBody.calculateBodyLength()));

        return headers;
    }

    /**
     * Create corresponding Connection Information message
     *
     * @param serverConfiguration - {@link ServerConfiguration} object which contains server IP address and server port
     * @param sequenceNumber      - communication sequence number
     * @param protocol            - according protocol
     * @param authorizationLevel  - according authorization level
     * @return - serialized Connection Information message
     */
    @NotNull
    public static String generateConnectionMessage(@NotNull ServerConfiguration serverConfiguration, int sequenceNumber,
                                                   @NotNull String protocol, @NotNull String authorizationLevel) {
        String message = SERVER_TAG + ": " + serverConfiguration.getIpAddress() + ":"
                + serverConfiguration.getPort()
                + COMMUNICATION_ENDPOINT + ". has information for you.";
        message += "\n" + SEQUENCE_NUMBER_TAG + ": " + sequenceNumber;
        message += "\n" + PROTOCOL_TAG + ": " + protocol;
        message += "\n" + AUTHORIZATION_TAG + ": " + authorizationLevel;

        return message;
    }
}
