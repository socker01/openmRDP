package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.exceptions.MessageDeserializeException;
import cz.vutbr.fit.openmrdp.security.AuthorizationLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class is used for deserialization of string messages into the {@link BaseMessage} objects.
 *
 * @author Jiri Koudelka
 * @since 10.02.2018.
 */
public final class MessageDeserializer {

    /**
     * Deserialize String message into the {@link BaseMessage} object
     *
     * @param serializedMessage - Message stored in the {@link String}
     * @return - {@link BaseMessage} deserialized message
     * @throws MessageDeserializeException - if the message doesn't have expected body
     */
    @NotNull
    public static BaseMessage deserializeMessage(@NotNull String serializedMessage) throws MessageDeserializeException {
        try {
            return deserialize(serializedMessage);
        } catch (IllegalArgumentException iae) {
            throw new MessageDeserializeException(iae.getMessage());
        } catch (NoSuchElementException | StringIndexOutOfBoundsException exc) {
            throw new MessageDeserializeException("Message does not have expected body");
        }
    }

    @NotNull
    private static BaseMessage deserialize(@NotNull String serializedMessage) {

        Scanner scanner = new Scanner(serializedMessage);

        String operationLineRaw = scanner.nextLine();
        OperationLine operationLine = getOperationLine(operationLineRaw);

        Map<HeaderType, String> messageHeaders = getHeadersForOperation(scanner, operationLine.getOperationType());

        String query = null;
        if (operationLine.getOperationType() == OperationType.IDENTIFY) {
            query = getMessageBody(scanner);
        }

        MessageBody messageBody = createMessageBody(query);

        return new BaseMessage(operationLine, messageHeaders, messageBody);
    }

    @Nullable
    private static MessageBody createMessageBody(@Nullable String query) {
        MessageBody messageBody = null;
        if (query != null) {
            messageBody = new MessageBody(query, ContentType.PLANT_QUERY);
        }

        return messageBody;
    }

    @NotNull
    private static OperationLine getOperationLine(@NotNull String operationLineRaw) {
        String operationRaw = operationLineRaw.substring(0, operationLineRaw.indexOf(' '));
        OperationType operationType = OperationType.fromString(operationRaw);

        String resourceName = getResourceName(operationLineRaw);

        MessageProtocol messageProtocol = getMessageProtocol(operationLineRaw);

        return new OperationLine(operationType, resourceName, messageProtocol);
    }

    @NotNull
    private static String getResourceName(@NotNull String operationLineRaw) {
        String resourceName = operationLineRaw.substring(operationLineRaw.indexOf(' '), operationLineRaw.lastIndexOf(' '));

        return resourceName.substring(1);
    }

    @NotNull
    private static MessageProtocol getMessageProtocol(@NotNull String operationLineRaw) {
        String messageProtocolRaw = operationLineRaw.substring(operationLineRaw.lastIndexOf(' ') + 1);

        return MessageProtocol.fromString(messageProtocolRaw);
    }

    @NotNull
    private static Map<HeaderType, String> getHeadersForOperation(@NotNull Scanner scanner, @NotNull OperationType operationType) {
        Map<HeaderType, String> messageHeaders = new HashMap<>();

        for (int i = 0; i < operationType.getHeadersCount(); i++) {
            String headerLine = scanner.nextLine();

            HeaderType headerType = getHeaderType(headerLine);
            String headerValue = getHeaderValue(headerLine);

            messageHeaders.put(headerType, headerValue);
        }

        return messageHeaders;
    }

    @NotNull
    private static HeaderType getHeaderType(@NotNull String headerLine) {
        String headerTypeRaw = headerLine.substring(0, headerLine.indexOf(':'));

        return HeaderType.fromString(headerTypeRaw);
    }

    @NotNull
    private static String getHeaderValue(@NotNull String headerLine) {
        return headerLine.substring(headerLine.indexOf(' ') + 1);
    }

    @Nullable
    private static String getMessageBody(@NotNull Scanner scanner) {
        StringBuilder queryBuilder = new StringBuilder();

        scanner.nextLine();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.equals("")) {
                queryBuilder.append(line);
                if (scanner.hasNextLine()) {
                    queryBuilder.append("\n");
                }
            }
        }

        return queryBuilder.toString();
    }

    @NotNull
    public static ConnectionInformationMessage deserializeMRDPServerResponseMessage(@NotNull String message) {
        MessageValidator.validateConnectionInformationMessage(message);
        String serverAddress = getServerAddress(message);
        MessageProtocol protocol = getCommunicationProtocol(message);
        AuthorizationLevel authorizationLevel = getAuthorizationLevel(message);

        return new ConnectionInformationMessage(serverAddress, protocol, authorizationLevel);
    }

    @NotNull
    private static MessageProtocol getCommunicationProtocol(@NotNull String message) {
        if (message.contains(MessageFactory.PROTOCOL_TAG + ": HTTPS")) {
            return MessageProtocol.HTTPS;
        } else {
            return MessageProtocol.HTTP;
        }
    }

    @NotNull
    private static AuthorizationLevel getAuthorizationLevel(@NotNull String message) {
        if (message.contains(MessageFactory.AUTHORIZATION_TAG + ": " + AuthorizationLevel.NONE.getCode())) {
            return AuthorizationLevel.NONE;
        } else {
            return AuthorizationLevel.REQUIRED;
        }
    }

    @NotNull
    private static String getServerAddress(@NotNull String message) {
        String parsedMessage = message.substring(MessageFactory.SERVER_TAG.length() + 3);
        int indexOfEnd = parsedMessage.indexOf(". ");

        return parsedMessage.substring(0, indexOfEnd);
    }
}
