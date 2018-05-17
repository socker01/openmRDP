package cz.vutbr.fit.openmrdp.communication;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;

import java.io.IOException;

/**
 * This class is used for receiving of message.
 *
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
interface MessageReceiver {

    /**
     * Receive messages
     *
     * @return - {@link BaseMessage} received message
     * @throws IOException - if there will be some problem with parsing of received data
     */
    @NotNull
    BaseMessage receiveMessages() throws IOException;
}
