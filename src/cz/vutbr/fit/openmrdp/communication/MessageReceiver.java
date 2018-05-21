package cz.vutbr.fit.openmrdp.communication;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.logger.MrdpLogger;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.ConnectionInformationMessage;

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

    /**
     * Receive connection information message
     * @param clientPort - communication port
     * @param logger - logger for logging of the potential error
     * @return - {@link ConnectionInformationMessage} with information about connection
     */
    @NotNull
    ConnectionInformationMessage receiveConnectionInformationMessage(int clientPort, @NotNull MrdpLogger logger) throws IOException;
}
