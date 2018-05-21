package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.exceptions.MessageDeserializeException;
import org.junit.Test;

public class MessageValidatorTest {

    private static final String VALID_CONNECTION_INFORMATION_MESSAGE = MessageFactory.SERVER_TAG + ": abc\n" +
            MessageFactory.PROTOCOL_TAG + ": HTTP\n" +
            MessageFactory.AUTHORIZATION_TAG + ": None";

    private static final String INVALID_CONNECTION_INFORMATION_MESSAGE = MessageFactory.PROTOCOL_TAG + ": HTTP\n" +
            MessageFactory.AUTHORIZATION_TAG + ": None";

    @Test
    public void validateConnectionInformationMessage() {
        MessageValidator.validateConnectionInformationMessage(VALID_CONNECTION_INFORMATION_MESSAGE);
    }

    @Test(expected = MessageDeserializeException.class)
    public void validateInvalidConnectionInformationMessage() {
        MessageValidator.validateConnectionInformationMessage(INVALID_CONNECTION_INFORMATION_MESSAGE);
    }

    @Test
    public void validateReDELMessage() {
        MessageValidator.validateReDELMessage(RedelMessageFactoryTest.EXPECTED_MESSAGE_BODY);
    }

    @Test(expected = MessageDeserializeException.class)
    public void validateInvalidReDELMessage() {
        String invalidRedelMessage = RedelMessageFactoryTest.EXPECTED_MESSAGE_BODY.replace(ReDELMessageBodyFactory.LOCATION_TAG, "test");
        MessageValidator.validateReDELMessage(invalidRedelMessage);
    }
}