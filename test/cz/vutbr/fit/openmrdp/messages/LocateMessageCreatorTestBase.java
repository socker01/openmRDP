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
final class LocateMessageCreatorTestBase extends MessageTestBase {

    @BeforeEach
    void setUp(){
        message = MessageCreator.createLocateMessage(TEST_RESOURCE_NAME, TEST_CALLBACK_URI);
    }

    @Test
    void testCreateLocateMessageOperationLine() {
        OperationLine operationLine = new OperationLine(OperationType.LOCATE, TEST_RESOURCE_NAME, MessageProtocol.MRDP);
        assertThat(message.getOperationLine(), is(operationLine));

        String operationLineString = OperationType.LOCATE.toString() + " " + TEST_RESOURCE_NAME + " " + MessageProtocol.MRDP.getProtocolCode();
        assertThat(message.getOperationLine().createOperationLineString(), is(operationLineString));
    }

    @Test
    void testCreateLocateMessageHeaders(){
        assertThat(message.getHeaders().size(), is(2));

        assertThat(message.getHeaders().keySet(), hasItem(HeaderType.NSEQ));
        assertThat(message.getHeaders().get(HeaderType.CALLBACK_URI), is(TEST_CALLBACK_URI));
    }

    @Test
    void testCreateLocateMessageBody(){
        assertThat(message.getBody(), is(nullValue()));
    }
}