package cz.vutbr.fit.openmrdp.messageprocessors;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;

/**
 * Process messages and return appropriate ReDEL message
 *
 * @author Jiri Koudelka
 * @since 27.03.2018.
 */
public interface MessageProcessor {

    /**
     * Process message a do appropriate business logic
     *
     * @param receivedMessage - {@link BaseMessage} object with message to process
     * @return - {@link BaseMessage} ReDEL message with response to client
     * @throws AddressSyntaxException - if the address in the input message has not correct syntax
     */
    @NotNull
    BaseMessage processMessage(@NotNull BaseMessage receivedMessage) throws AddressSyntaxException;
}
