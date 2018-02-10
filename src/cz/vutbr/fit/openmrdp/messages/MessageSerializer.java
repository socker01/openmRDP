package cz.vutbr.fit.openmrdp.messages;

/**
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
public final class MessageSerializer {

    public static String serializeMessage(BaseMessage message) {
        StringBuilder serializedMessage = new StringBuilder();

        serializedMessage.append(message.getOperationLine().createOperationLineString());
        serializedMessage.append("\n");

        for(HeaderType headerType : message.getHeaders().keySet()){
            serializedMessage.append(headerType.getHeaderCode());
            serializedMessage.append(": ");
            serializedMessage.append(message.getHeaders().get(headerType));
            serializedMessage.append("\n");
        }

        if(message.getBody() != null){
            serializedMessage.append("\n");
            serializedMessage.append(message.getBody());
        }

        return serializedMessage.toString();
    }
}
