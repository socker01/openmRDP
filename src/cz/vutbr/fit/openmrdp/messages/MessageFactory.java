package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.server.ServerConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public final class MessageFactory {

    public static final long MAX_SEQUENCE_NUMBER = 2147483648L;    //TCP standard - 2^31
    private static final String SEQUENCE_NUMBER_TAG = "NSEQ";

    static final String SERVER_TAG = "SERVER";
    static final String PROTOCOL_TAG = "PROTOCOL";
    static final String AUTHORIZATION_TAG = "AUTHORIZATION";

    public static BaseMessage createLocateMessage(String resourceName, String callbackURI, long sequenceNumber) {
        OperationLine operationLine = new OperationLine(OperationType.LOCATE, resourceName, MessageProtocol.MRDP);

        return new BaseMessage(operationLine, createBasicMessageHeaders(callbackURI, sequenceNumber), null);
    }

    private static Map<HeaderType, String> createBasicMessageHeaders(String callbackURI, long sequenceNumber) {
        Map<HeaderType, String> headers = new HashMap<>();

        headers.put(HeaderType.NSEQ, String.valueOf(sequenceNumber));
        headers.put(HeaderType.CALLBACK_URI, callbackURI);

        return headers;
    }

    public static BaseMessage createIdentifyMessage(String resourceName, String callbackURI, MessageBody body, long sequenceNumber) {
        OperationLine operationLine = new OperationLine(OperationType.IDENTIFY, resourceName, MessageProtocol.MRDP);

        Map<HeaderType, String> headers = createLocateMessageHeaders(callbackURI, body, sequenceNumber);

        return new BaseMessage(operationLine, headers, body);
    }

    private static Map<HeaderType, String> createLocateMessageHeaders(String callbackURI, MessageBody body, long sequenceNumber) {
        Map<HeaderType, String> headers = createBasicMessageHeaders(callbackURI, sequenceNumber);

        headers.put(HeaderType.CONTENT_TYPE, body.getContentType().getCode());
        headers.put(HeaderType.CONTENT_LENGTH, String.valueOf(body.calculateBodyLength()));

        return headers;
    }

    public static BaseMessage createReDELResponse(ReDELResponseDTO responseDTO) {
        OperationLine operationLine = new OperationLine(OperationType.POST, responseDTO.getAddress().getEndPoint(), MessageProtocol.HTTP);

        MessageBody messageBody = ReDELMessageBodyCreator.createRedelMessage(responseDTO.getResources());

        Map<HeaderType, String> headers = createReDELMessageHeaders(responseDTO, messageBody);

        return new BaseMessage(operationLine, headers, messageBody);
    }

    private static Map<HeaderType, String> createReDELMessageHeaders(ReDELResponseDTO responseDTO, @Nullable MessageBody messageBody) {
        Map<HeaderType, String> headers = new HashMap<>();

        headers.put(HeaderType.HOST, responseDTO.getAddress().getHostAddress());
        headers.put(HeaderType.NSEQ, String.valueOf(responseDTO.getSequenceNumber()));
        headers.put(HeaderType.CONTENT_TYPE, messageBody.getContentType().getCode());
        headers.put(HeaderType.CONTENT_LENGTH, messageBody.getQuery() == null ? String.valueOf(0) : String.valueOf(messageBody.calculateBodyLength()));

        return headers;
    }

    public static String generateConnectionMessage(ServerConfiguration serverConfiguration, int sequenceNumber, String protocol, String authorizationLevel){
        String message = SERVER_TAG + ": " + serverConfiguration.getIpAddress() + ":" + serverConfiguration.getPort() + "/auth. has information for you.";
        message += "\n" + SEQUENCE_NUMBER_TAG + ": " + sequenceNumber;
        message += "\n" + PROTOCOL_TAG + ": " + protocol;
        message += "\n" + AUTHORIZATION_TAG + ": " + authorizationLevel;

        return message;
    }
}
