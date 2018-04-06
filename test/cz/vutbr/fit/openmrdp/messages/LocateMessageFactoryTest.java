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
public final class LocateMessageFactoryTest {

    private BaseMessage message;

    @Before
    public void setUp(){
        message = MessageFactory.createLocateMessage(MessageTestConstants.TEST_RESOURCE_NAME, MessageTestConstants.TEST_CALLBACK_URI);
    }

    @Test
    public void createLocateMessageOperationLine() {
        OperationLine operationLine = new OperationLine(OperationType.LOCATE, MessageTestConstants.TEST_RESOURCE_NAME, MessageProtocol.MRDP);
        assertThat(message.getOperationLine(), is(operationLine));

        String operationLineString = OperationType.LOCATE.toString() + " " + MessageTestConstants.TEST_RESOURCE_NAME + " " + MessageProtocol.MRDP.getProtocolCode();
        assertThat(message.getOperationLine().createOperationLineString(), is(operationLineString));
    }

    @Test
    public void createLocateMessageHeaders(){
        assertThat(message.getHeaders().size(), is(OperationType.LOCATE.getHeadersCount()));

        assertThat(message.getHeaders().keySet(), hasItem(HeaderType.NSEQ));
        assertThat(message.getHeaders().get(HeaderType.CALLBACK_URI), is(MessageTestConstants.TEST_CALLBACK_URI));
    }

    @Test
    public void createLocateMessageBody(){
        assertThat(message.getBodyQuery(), is(nullValue()));
    }
}