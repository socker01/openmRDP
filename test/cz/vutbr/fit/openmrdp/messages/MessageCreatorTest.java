package cz.vutbr.fit.openmrdp.messages;

import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
final class MessageCreatorTest {

    private static final String TEST_RESOURCE_NAME = "urn:uuid:testResource";
    private static final String TEST_CALLBACK_URI = "192.168.0.1/testcallbackuri";

    @Test
    void createLocateMessage() {
        BaseMessage locateMessage = MessageCreator.createLocateMessage(TEST_RESOURCE_NAME, TEST_CALLBACK_URI);

        assertThat(locateMessage.getOperationType(), is(OperationType.LOCATE));
    }

}