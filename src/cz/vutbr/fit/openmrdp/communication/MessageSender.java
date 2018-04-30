package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.address.Address;
import cz.vutbr.fit.openmrdp.server.ServerConfiguration;

import java.io.IOException;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
interface MessageSender {

    void sendMRDPMessage(BaseMessage message) throws IOException;

    void sendReDELMessage(BaseMessage message) throws IOException;

    void sendInformationAboutNonSecureConnection(Address clientAddress, ServerConfiguration serverConfiguration, int sequenceNumber) throws IOException;

    void sendInformationAboutSecureConnection(Address clientAddress, ServerConfiguration serverConfiguration, int sequenceNumber) throws IOException;
}
