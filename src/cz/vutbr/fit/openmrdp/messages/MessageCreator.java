package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public final class MessageCreator {

    private static final long MAX_SEQUENCE_NUMBER = 2147483648L;    //TCP standard - 2^31

    public static BaseMessage createLocateMessage(String resourceName, String callbackURI){
        OperationLine operationLine = new OperationLine(OperationType.LOCATE, resourceName, MessageProtocol.MRDP);

        return new BaseMessage(operationLine, createBasicMessageHeaders(callbackURI), null);
    }

    private static Map<HeaderType, String> createBasicMessageHeaders(String callbackURI){
        Map<HeaderType, String> headers = new HashMap<>();

        long sequenceNumber = generateSequenceNumber();

        headers.put(HeaderType.NSEQ, String.valueOf(sequenceNumber));
        headers.put(HeaderType.CALLBACK_URI, callbackURI);

        return headers;
    }

    private static long generateSequenceNumber(){
        return (long) (Math.random() * MAX_SEQUENCE_NUMBER + 1);
    }

    public static BaseMessage createIdentifyMessage(String resourceName, String callbackURI, MessageBody body) {
        OperationLine operationLine = new OperationLine(OperationType.IDENTIFY, resourceName, MessageProtocol.MRDP);

        Map<HeaderType, String> headers = createLocateMessageHeaders(callbackURI, body);

        return new BaseMessage(operationLine, headers, body.getQuery());
    }

    private static Map<HeaderType, String> createLocateMessageHeaders(String callbackURI, MessageBody body){
        Map<HeaderType, String> headers = createBasicMessageHeaders(callbackURI);

        headers.put(HeaderType.CONTENT_TYPE, body.getContentType().getCode());
        headers.put(HeaderType.CONTENT_LENGTH, String.valueOf(body.calculateBodyLength()));

        return headers;
    }

    public static BaseMessage createReDELResponse(ReDELResponseDTO responseDTO){
        OperationLine operationLine = new OperationLine(OperationType.POST, responseDTO.getAddress().getEndPoint(), MessageProtocol.HTTP);

        //TODO: create response body with location information
        String responseBody = "";
        MessageBody messageBody = new MessageBody(responseBody, ContentType.REDEL);

        Map<HeaderType, String> headers = createReDELMessageHeaders(responseDTO, messageBody);

        return new BaseMessage(operationLine, headers, messageBody.getQuery());
    }

    private static Map<HeaderType, String> createReDELMessageHeaders(ReDELResponseDTO responseDTO, MessageBody messageBody){
        Map<HeaderType, String> headers = new HashMap<>();

        headers.put(HeaderType.HOST, responseDTO.getAddress().getHostAddress());
        //TODO: same nseq as is in request?
        headers.put(HeaderType.NSEQ, String.valueOf(responseDTO.getSequenceNumber()));
        headers.put(HeaderType.CONTENT_TYPE, messageBody.getContentType().getCode());
        headers.put(HeaderType.CONTENT_LENGTH, String.valueOf(messageBody.calculateBodyLength()));

        return headers;
    }
}
