package cz.vutbr.fit.openmrdp.communication;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.address.Address;

import java.io.IOException;

/**
 * Service for message sending.
 *
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
interface MessageSender {

    /**
     * Send mRDP message with request
     *
     * @param message - {@link BaseMessage} message to send
     * @throws IOException - if there will be problem with sending of the message
     */
    void sendMRDPMessage(@NotNull BaseMessage message) throws IOException;

    /**
     * Send ConnectionInformation message with the information about connection
     *
     * @param message - {@link BaseMessage} message to send
     * @throws IOException - if there will be problem with sending of the message
     */
    void sendInformationAboutConnection(@NotNull Address clientAddress, @NotNull String message) throws IOException;
}
