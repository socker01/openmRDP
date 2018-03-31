package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public final class MessageCreator {

    private static final long MAX_SEQUENCE_NUMBER = 2147483648L;    //TCP standard - 2^31

    public static BaseMessage createLocateMessage(String resourceName, String callbackURI) {
        OperationLine operationLine = new OperationLine(OperationType.LOCATE, resourceName, MessageProtocol.MRDP);

        return new BaseMessage(operationLine, createBasicMessageHeaders(callbackURI), null);
    }

    private static Map<HeaderType, String> createBasicMessageHeaders(String callbackURI) {
        Map<HeaderType, String> headers = new HashMap<>();

        long sequenceNumber = generateSequenceNumber();

        headers.put(HeaderType.NSEQ, String.valueOf(sequenceNumber));
        headers.put(HeaderType.CALLBACK_URI, callbackURI);

        return headers;
    }

    private static long generateSequenceNumber() {
        return (long) (Math.random() * MAX_SEQUENCE_NUMBER + 1);
    }

    public static BaseMessage createIdentifyMessage(String resourceName, String callbackURI, MessageBody body) {
        OperationLine operationLine = new OperationLine(OperationType.IDENTIFY, resourceName, MessageProtocol.MRDP);

        Map<HeaderType, String> headers = createLocateMessageHeaders(callbackURI, body);

        return new BaseMessage(operationLine, headers, body);
    }

    private static Map<HeaderType, String> createLocateMessageHeaders(String callbackURI, MessageBody body) {
        Map<HeaderType, String> headers = createBasicMessageHeaders(callbackURI);

        headers.put(HeaderType.CONTENT_TYPE, body.getContentType().getCode());
        headers.put(HeaderType.CONTENT_LENGTH, String.valueOf(body.calculateBodyLength()));

        return headers;
    }

    public static BaseMessage createReDELResponse(ReDELResponseDTO responseDTO) {
        OperationLine operationLine = new OperationLine(OperationType.POST, responseDTO.getAddress().getEndPoint(), MessageProtocol.HTTP);

        MessageBody messageBody = ReDELMessageBodyCreator.createRedelMessage(responseDTO.getResourceUri(), responseDTO.getResourceLocation());

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
}
