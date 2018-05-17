package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.exceptions.MessageDeserializeException;

/**
 * Validator used for message validating.
 *
 * Throws {@link MessageDeserializeException} if the message has incorrect syntax.
 *
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
final class MessageValidator {

    static void validateReDELMessage(String message) {
        if (!message.contains(ReDELMessageBodyFactory.LOCATION_TAG)) {
            throw new MessageDeserializeException("Response from the server doesn't have expected body: " + message);
        }
    }

    static void validateConnectionInformationMessage(String message){
        if (!message.contains(MessageFactory.SERVER_TAG)
                || !message.contains(MessageFactory.PROTOCOL_TAG)
                || !message.contains(MessageFactory.AUTHORIZATION_TAG)) {
            throw new MessageDeserializeException("ConnectionInformationMessage doesn't have expected body.");
        }
    }
}
