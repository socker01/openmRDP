package cz.vutbr.fit.openmrdp.model;

import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 01.03.2018.
 */
interface InformationBaseService {

    Set<RDFTriple> loadInformationBase();

    void addInformationToBase(RDFTriple triple);
}
