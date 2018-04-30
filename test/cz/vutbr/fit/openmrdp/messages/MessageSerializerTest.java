package cz.vutbr.fit.openmrdp.messages;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
public final class MessageSerializerTest {

    private BaseMessage message;

    private static final String EXPECTED_OPERATION_LINE = "LOCATE ?testResource mRDP/1.0";
    private static final String EXPECTED_CALLBACK_HEADER = HeaderType.CALLBACK_URI.getHeaderCode() + ": " + MessageTestConstants.TEST_CALLBACK_URI;

    @Before
    public void setUp(){
        message = MessageFactory.createLocateMessage(MessageTestConstants.TEST_RESOURCE_NAME, MessageTestConstants.TEST_CALLBACK_URI, 1);
    }

    @Test
    public void serializeMessage() {
        String serializedMessage = MessageSerializer.serializeMessage(message);

        assertThat(serializedMessage, containsString(EXPECTED_OPERATION_LINE));
        assertThat(serializedMessage, containsString(EXPECTED_CALLBACK_HEADER));
        assertThat(serializedMessage, containsString(HeaderType.NSEQ.getHeaderCode()));
    }


}