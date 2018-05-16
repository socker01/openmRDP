package cz.vutbr.fit.openmrdp.communication;

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
    BaseMessage receiveMessages() throws IOException;
}
