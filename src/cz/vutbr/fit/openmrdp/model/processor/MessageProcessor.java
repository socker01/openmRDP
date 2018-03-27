package cz.vutbr.fit.openmrdp.model.processor;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;

/**
 * @author Jiri Koudelka
 * @since 27.03.2018.
 */
public interface MessageProcessor {

    BaseMessage processMessage(BaseMessage receivedMessage) throws AddressSyntaxException;
}
