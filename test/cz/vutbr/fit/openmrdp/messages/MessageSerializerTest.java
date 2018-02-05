package cz.vutbr.fit.openmrdp.messages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
final class MessageSerializerTest extends MessageTestBase{

    private static final String EXPECTED_OPERATION_LINE = "LOCATE ?testResource mRDP/1.0";
    private static final String EXPECTED_CALLBACK_HEADER = HeaderType.CALLBACK_URI.getHeaderCode() + ": " + TEST_CALLBACK_URI;

    @BeforeEach
    void setUp(){
        message = MessageCreator.createLocateMessage(TEST_RESOURCE_NAME, TEST_CALLBACK_URI);
    }

    @Test
    void serializeMessage() {
        String serializedMessage = MessageSerializer.serializeMessage(message);

        assertThat(serializedMessage, containsString(EXPECTED_OPERATION_LINE));
        assertThat(serializedMessage, containsString(EXPECTED_CALLBACK_HEADER));
        assertThat(serializedMessage, containsString(HeaderType.NSEQ.getHeaderCode()));
    }


}