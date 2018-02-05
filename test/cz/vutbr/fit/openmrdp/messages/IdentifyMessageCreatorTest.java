package cz.vutbr.fit.openmrdp.messages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
final class IdentifyMessageCreatorTest {
    private static final String TEST_RESOURCE_NAME = "?testResource";
    private static final String TEST_CALLBACK_URI = "192.168.0.1/testUri";
    private static final String TEST_QUERY = "?testResource <http://www.awareit.com/onto/2005/12/locationlocatedIn> ?room";

    private BaseMessage identifyMessage;
    private MessageBody messageBody = new MessageBody(TEST_QUERY, ContentType.PLANT_QUERY);


    @BeforeEach
    void setUp(){
        identifyMessage = MessageCreator.createIdentifyMessage(TEST_RESOURCE_NAME, TEST_CALLBACK_URI, messageBody);
    }

    @Test
    void testCreateIdentifyMessageOperationLine() {
        OperationLine operationLine = new OperationLine(OperationType.IDENTIFY, TEST_RESOURCE_NAME, MessageProtocol.MRDP);
        assertThat(identifyMessage.getOperationLine(), is(operationLine));

        String operationLineString = OperationType.IDENTIFY.toString() + " " + TEST_RESOURCE_NAME + " " + MessageProtocol.MRDP.getProtocolCode();
        assertThat(identifyMessage.getOperationLine().createOperationLineString(), is(operationLineString));
    }

    @Test
    void testCreateIdentifyMessageHeaders(){
        assertThat(identifyMessage.getHeaders().size(), is(4));

        assertThat(identifyMessage.getHeaders().keySet(), hasItem(HeaderType.NSEQ));
        assertThat(identifyMessage.getHeaders().get(HeaderType.CALLBACK_URI), is(TEST_CALLBACK_URI));
        assertThat(identifyMessage.getHeaders().get(HeaderType.CONTENT_TYPE), is(ContentType.PLANT_QUERY.getCode()));

        byte[] bodyBytes = TEST_QUERY.getBytes(Charset.forName(MessageBody.DEFAULT_QUERY_CHARSET));
        int expectedBodyLength = bodyBytes.length;
        assertThat(identifyMessage.getHeaders().get(HeaderType.CONTENT_LENGTH), is(String.valueOf(expectedBodyLength)));
    }

    @Test
    void testCreateLocateMessageBody(){
        assertThat(identifyMessage.getBody(), is(TEST_QUERY));
    }
}