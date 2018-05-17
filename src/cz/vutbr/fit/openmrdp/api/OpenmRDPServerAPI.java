package cz.vutbr.fit.openmrdp.api;

import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;

/**
 * Public server interface of OpenmRDP library
 *
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public interface OpenmRDPServerAPI {

    /**
     * Start receiving of the mRDP messages
     *
     * @throws NetworkCommunicationException - if there will be some problem with network
     * @throws AddressSyntaxException - if the client address have incorrect syntax
     */
    void receiveMessages() throws NetworkCommunicationException, AddressSyntaxException;

    /**
     * Add information into the information base. This method also affect location tree and save information into the informationBase.xml
     *
     * @param information - {@link RDFTriple with information}
     */
    void addInformationToInformationBase(@NotNull RDFTriple information);

    /**
     * Remove information from the information base. This method also affect location tree and remove information from the informationBase.xml
     *
     * @param information - {@link RDFTriple with the information}
     */
    void removeInformationFromInformationBase(@NotNull RDFTriple information);
}
