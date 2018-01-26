package cz.vutbr.fit.openmrdp.messages;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public final class MessageCreator {

    private static final long MAX_SEQUENCE_NUMBER = 2147483648L;    //TCP standard - 2^31

    public static BaseMessage createLocateMessage(String resourceName, String callbackURI){
        BaseMessage.Builder builder = new BaseMessage.Builder();

        return builder.operationType(OperationType.LOCATE)
                .headers(createLocateMessageHeaders(callbackURI))
                .resource(resourceName)
                .build();
    }

    private static Map<HeaderType, String> createLocateMessageHeaders(String callbackURI){
        Map<HeaderType, String> headers = new HashMap<>();

        long sequenceNumber = generateSequenceNumber();

        headers.put(HeaderType.NSEQ, String.valueOf(sequenceNumber));
        headers.put(HeaderType.CALLBACK_URI, callbackURI);

        return headers;
    }

    private static long generateSequenceNumber(){
        return (long) (Math.random() * MAX_SEQUENCE_NUMBER + 1);
    }
}
