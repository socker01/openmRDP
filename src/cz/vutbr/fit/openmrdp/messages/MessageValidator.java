package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.exceptions.MessageDeserializeException;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
final class MessageValidator {

    static void validateReDELMessage(String message) {
        if (!message.contains(ReDELMessageBodyCreator.LOCATION_TAG)) {
            throw new MessageDeserializeException("Response from the server doesn't have expected body: " + message);
        }
    }

    static void validateMRDPServerResponseMessage(String message){
        if (!message.contains(MessageFactory.SERVER_TAG)
                || !message.contains(MessageFactory.PROTOCOL_TAG)
                || !message.contains(MessageFactory.AUTHORIZATION_TAG)) {
            throw new MessageDeserializeException("MRDPServerResponseMessage doesn't have expected body.");
        }
    }
}
