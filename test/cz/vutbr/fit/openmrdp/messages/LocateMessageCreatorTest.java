package cz.vutbr.fit.openmrdp.messages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
final class LocateMessageCreatorTest {

    private static final String TEST_RESOURCE_NAME = "urn:uuid:testResource";
    private static final String TEST_CALLBACK_URI = "192.168.0.1/testcallbackuri";

    private BaseMessage baseMessage;

    @BeforeEach
    void setUp(){
        baseMessage = MessageCreator.createLocateMessage(TEST_RESOURCE_NAME, TEST_CALLBACK_URI);
    }

    @Test
    void testCreateLocateMessageOperationLine() {
        OperationLine operationLine = new OperationLine(OperationType.LOCATE, TEST_RESOURCE_NAME, MessageProtocol.MRDP);
        assertThat(baseMessage.getOperationLine(), is(operationLine));

        String operationLineString = OperationType.LOCATE.toString() + " " + TEST_RESOURCE_NAME + " " + MessageProtocol.MRDP.getProtocolCode();
        assertThat(baseMessage.getOperationLine().createOperationLineString(), is(operationLineString));
    }

    @Test
    void testCreateLocateMessageHeaders(){
        assertThat(baseMessage.getHeaders().size(), is(2));

        assertThat(baseMessage.getHeaders().keySet(), hasItem(HeaderType.NSEQ));
        assertThat(baseMessage.getHeaders().get(HeaderType.CALLBACK_URI), is(TEST_CALLBACK_URI));
    }

    @Test
    void testCreateLocateMessageBody(){
        assertThat(baseMessage.getBody(), is(nullValue()));
    }
}