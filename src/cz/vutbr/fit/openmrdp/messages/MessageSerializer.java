package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.NotNull;

/**
 * This class is used for serializing of {@link BaseMessage} objects to the {@link} String
 *
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
public final class MessageSerializer {

    /**
     * Serialize IDENTIFY or LOCATE message to the {@link String}
     *
     * @param message - {@link BaseMessage} to serialize
     * @return - {@link String}
     */
    @NotNull
    public static String serializeMessage(@NotNull BaseMessage message) {
        StringBuilder serializedMessage = new StringBuilder();

        serializedMessage.append(message.getOperationLine().createOperationLineString());
        serializedMessage.append("\n");

        for(HeaderType headerType : message.getHeaders().keySet()){
            serializedMessage.append(headerType.getHeaderCode());
            serializedMessage.append(": ");
            serializedMessage.append(message.getHeaders().get(headerType));
            serializedMessage.append("\n");
        }

        if(message.getBodyQuery() != null){
            serializedMessage.append("\n");
            serializedMessage.append(message.getBodyQuery());
        }

        return serializedMessage.toString();
    }
}
