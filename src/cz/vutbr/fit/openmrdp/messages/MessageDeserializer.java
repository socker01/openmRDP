package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.exceptions.MessageDeserializeException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Jiri Koudelka
 * @since 10.02.2018.
 */
public final class MessageDeserializer {

    public static BaseMessage deserializeMessage(String serializedMessage) throws MessageDeserializeException {
        try{
            return deserialize(serializedMessage);
        }catch (NoSuchElementException nse){
            throw new MessageDeserializeException("Message does not have expected body");
        }
    }

    private static BaseMessage deserialize(String serializedMessage){

        Scanner scanner = new Scanner(serializedMessage);

        String operationLineRaw = scanner.nextLine();
        OperationLine operationLine = getOperationLine(operationLineRaw);

        Map<HeaderType, String> messageHeaders = getHeadersForOperation(scanner, operationLine.getOperationType());

        String query = null;
        if(operationLine.getOperationType() == OperationType.IDENTIFY){
            query = getMessageBody(scanner);
        }

        MessageBody messageBody = createMessageBody(query);

        return new BaseMessage(operationLine, messageHeaders, messageBody);
    }

    private static MessageBody createMessageBody(String query) {
        MessageBody messageBody = null;
        if(query != null) {
            messageBody = new MessageBody(query, ContentType.PLANT_QUERY);
        }

        return messageBody;
    }

    private static OperationLine getOperationLine(String operationLineRaw){
        String operationRaw = operationLineRaw.substring(0, operationLineRaw.indexOf(' '));
        OperationType operationType = OperationType.fromString(operationRaw);

        String resourceName = getResourceName(operationLineRaw);

        MessageProtocol messageProtocol = getMessageProtocol(operationLineRaw);

        return new OperationLine(operationType, resourceName, messageProtocol);
    }

    private static String getResourceName(String operationLineRaw) {
        String resourceName = operationLineRaw.substring(operationLineRaw.indexOf(' '), operationLineRaw.lastIndexOf(' '));

        return resourceName.substring(1);
    }

    private static MessageProtocol getMessageProtocol(String operationLineRaw) {
        String messageProtocolRaw = operationLineRaw.substring(operationLineRaw.lastIndexOf(' ') + 1);

        return MessageProtocol.fromString(messageProtocolRaw);
    }

    private static Map<HeaderType, String> getHeadersForOperation(Scanner scanner, OperationType operationType){
        Map<HeaderType, String> messageHeaders = new HashMap<>();

        for (int i = 0; i < operationType.getHeadersCount(); i++){
            String headerLine = scanner.nextLine();

            HeaderType headerType = getHeaderType(headerLine);
            String headerValue = getHeaderValue(headerLine);

            messageHeaders.put(headerType, headerValue);
        }

        return messageHeaders;
    }

    private static HeaderType getHeaderType(String headerLine){
        String headerTypeRaw = headerLine.substring(0, headerLine.indexOf(':'));

        return HeaderType.fromString(headerTypeRaw);
    }

    private static String getHeaderValue(String headerLine){
        return headerLine.substring(headerLine.indexOf(' ') + 1);
    }

    private static String getMessageBody(Scanner scanner) {
        StringBuilder queryBuilder = new StringBuilder();

        scanner.nextLine();

        while (scanner.hasNextLine()){
            queryBuilder.append(scanner.nextLine());
        }

        return queryBuilder.toString();
    }
}
