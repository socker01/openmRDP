package cz.vutbr.fit.openmrdp.messages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
final class IdentifyMessageCreatorTestBase extends MessageTestBase {

    private MessageBody messageBody = new MessageBody(TEST_QUERY, ContentType.PLANT_QUERY);

    @BeforeEach
    void setUp(){
        message = MessageCreator.createIdentifyMessage(TEST_RESOURCE_NAME, TEST_CALLBACK_URI, messageBody);
    }

    @Test
    void testCreateIdentifyMessageOperationLine() {
        OperationLine operationLine = new OperationLine(OperationType.IDENTIFY, TEST_RESOURCE_NAME, MessageProtocol.MRDP);
        assertThat(message.getOperationLine(), is(operationLine));

        String operationLineString = OperationType.IDENTIFY.toString() + " " + TEST_RESOURCE_NAME + " " + MessageProtocol.MRDP.getProtocolCode();
        assertThat(message.getOperationLine().createOperationLineString(), is(operationLineString));
    }

    @Test
    void testCreateIdentifyMessageHeaders(){
        assertThat(message.getHeaders().size(), is(4));

        assertThat(message.getHeaders().keySet(), hasItem(HeaderType.NSEQ));
        assertThat(message.getHeaders().get(HeaderType.CALLBACK_URI), is(TEST_CALLBACK_URI));
        assertThat(message.getHeaders().get(HeaderType.CONTENT_TYPE), is(ContentType.PLANT_QUERY.getCode()));

        byte[] bodyBytes = TEST_QUERY.getBytes(Charset.forName(MessageBody.DEFAULT_QUERY_CHARSET));
        int expectedBodyLength = bodyBytes.length;
        assertThat(message.getHeaders().get(HeaderType.CONTENT_LENGTH), is(String.valueOf(expectedBodyLength)));
    }

    @Test
    void testCreateLocateMessageBody(){
        assertThat(message.getBody(), is(TEST_QUERY));
    }
}