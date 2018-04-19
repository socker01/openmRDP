package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public interface OpenmRDPServerAPI {

    void receiveMessages() throws NetworkCommunicationException, AddressSyntaxException;

    void addInformationToInformationBase(RDFTriple information);

    void removeInformationFromInformationBase(RDFTriple information);
}
