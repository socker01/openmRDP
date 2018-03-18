package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public interface OpenmRDPServerAPI {

    void receiveIncomingMessages() throws NetworkCommunicationException, AddressSyntaxException;

    void addInformationToInformationModel(String subject, String predicate, String object);
}
