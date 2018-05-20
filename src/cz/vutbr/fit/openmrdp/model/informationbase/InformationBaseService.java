package cz.vutbr.fit.openmrdp.model.informationbase;

import cz.vutbr.fit.openmrdp.model.base.RDFTriple;

import java.util.Set;

/**
 * Service interface for loading information base and adding or removing information to or from information base.
 *
 * @author Jiri Koudelka
 * @since 01.03.2018.
 */
public interface InformationBaseService {

    /**
     * Load information from information base
     *
     * @return {@link Set} of the {@link RDFTriple} which contains whole loaded information base
     */
    Set<RDFTriple> loadInformationBase();

    /**
     * Add information into the information base
     *
     * @param triple {@link RDFTriple} with information
     */
    void addInformationToBase(RDFTriple triple);

    /**
     * Remove information from information base
     *
     * @param triple {@link RDFTriple} with information
     */
    void removeInformationFromBase(RDFTriple triple);
}
