package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.exceptions.MessageDeserializeException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 10.02.2018.
 */
final class MessageDeserializerTest {

    @Test
    void testDeserializeLocateMessage() throws MessageDeserializeException {
        BaseMessage message = MessageCreator.createLocateMessage(MessageTestConstants.TEST_RESOURCE_NAME, MessageTestConstants.TEST_CALLBACK_URI);

        BaseMessage deserializedMessage = makeSerializationAndDeserializationProcess(message);

        assertLocateOperationLine(deserializedMessage);
        assertLocateMessageHeaders(deserializedMessage.getHeaders());
    }

    @Test
    void testDeserializeIdentifyMessage() throws MessageDeserializeException {
        MessageBody messageBody = new MessageBody(MessageTestConstants.TEST_QUERY, ContentType.PLANT_QUERY);
        BaseMessage message = MessageCreator.createIdentifyMessage(MessageTestConstants.TEST_RESOURCE_NAME, MessageTestConstants.TEST_CALLBACK_URI, messageBody);

        BaseMessage deserializedMessage = makeSerializationAndDeserializationProcess(message);

        assertIdentifyOperationLine(deserializedMessage);
        assertIdentifyMessageHeaders(deserializedMessage.getHeaders(), messageBody.calculateBodyLength());
        assertThat(deserializedMessage.getBodyQuery(), is(MessageTestConstants.TEST_QUERY));
    }

    private void assertLocateOperationLine(BaseMessage deserializedMessage) {
        OperationLine expectedOperationLine = new OperationLine(OperationType.LOCATE, MessageTestConstants.TEST_RESOURCE_NAME, MessageProtocol.MRDP);
        assertThat(deserializedMessage.getOperationLine(), is(expectedOperationLine));
    }

    private BaseMessage makeSerializationAndDeserializationProcess(BaseMessage message) throws MessageDeserializeException {
        String serializedMessage = MessageSerializer.serializeMessage(message);
        return MessageDeserializer.deserializeMessage(serializedMessage);
    }

    private void assertLocateMessageHeaders(Map<HeaderType, String> messageHeaders) {
        assertThat(messageHeaders.size(), is(2));
        assertThat(messageHeaders.keySet(), hasItem(HeaderType.NSEQ));
        assertThat(messageHeaders.keySet(), hasItem(HeaderType.CALLBACK_URI));

        assertThat(messageHeaders.get(HeaderType.CALLBACK_URI), is(MessageTestConstants.TEST_CALLBACK_URI));
    }

    private void assertIdentifyOperationLine(BaseMessage deserializedMessage) {
        OperationLine expectedOperationLine =  new OperationLine(OperationType.IDENTIFY, MessageTestConstants.TEST_RESOURCE_NAME, MessageProtocol.MRDP);
        assertThat(deserializedMessage.getOperationLine(), is(expectedOperationLine));
    }

    private void assertIdentifyMessageHeaders(Map<HeaderType, String> messageHeaders, int expectedContentLength) {
        assertThat(messageHeaders.size(), is(4));
        assertThat(messageHeaders.keySet(), hasItem(HeaderType.NSEQ));
        assertThat(messageHeaders.keySet(), hasItem(HeaderType.CALLBACK_URI));
        assertThat(messageHeaders.keySet(), hasItem(HeaderType.CONTENT_LENGTH));
        assertThat(messageHeaders.keySet(), hasItem(HeaderType.CONTENT_TYPE));

        assertThat(messageHeaders.get(HeaderType.CALLBACK_URI), is(MessageTestConstants.TEST_CALLBACK_URI));
        assertThat(new Integer(messageHeaders.get(HeaderType.CONTENT_LENGTH)), is(expectedContentLength));
        assertThat(messageHeaders.get(HeaderType.CONTENT_TYPE), is(ContentType.PLANT_QUERY.getCode()));
    }
}