package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.address.Address;

import java.io.IOException;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
interface MessageSender {

    void sendMRDPMessage(BaseMessage message) throws IOException;

    void sendReDELMessage(BaseMessage message) throws IOException;

    void sendInformationAboutConnection(Address clientAddress, String message) throws IOException;
}
