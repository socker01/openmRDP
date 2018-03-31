package cz.vutbr.fit.openmrdp.messages;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public final class LocateMessageCreatorTest {

    private BaseMessage message;

    @Before
    public void setUp(){
        message = MessageCreator.createLocateMessage(MessageTestConstants.TEST_RESOURCE_NAME, MessageTestConstants.TEST_CALLBACK_URI);
    }

    @Test
    public void testCreateLocateMessageOperationLine() {
        OperationLine operationLine = new OperationLine(OperationType.LOCATE, MessageTestConstants.TEST_RESOURCE_NAME, MessageProtocol.MRDP);
        assertThat(message.getOperationLine(), is(operationLine));

        String operationLineString = OperationType.LOCATE.toString() + " " + MessageTestConstants.TEST_RESOURCE_NAME + " " + MessageProtocol.MRDP.getProtocolCode();
        assertThat(message.getOperationLine().createOperationLineString(), is(operationLineString));
    }

    @Test
    public void testCreateLocateMessageHeaders(){
        assertThat(message.getHeaders().size(), is(OperationType.LOCATE.getHeadersCount()));

        assertThat(message.getHeaders().keySet(), hasItem(HeaderType.NSEQ));
        assertThat(message.getHeaders().get(HeaderType.CALLBACK_URI), is(MessageTestConstants.TEST_CALLBACK_URI));
    }

    @Test
    public void testCreateLocateMessageBody(){
        assertThat(message.getBodyQuery(), is(nullValue()));
    }
}