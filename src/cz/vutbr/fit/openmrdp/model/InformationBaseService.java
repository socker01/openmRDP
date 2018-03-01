package cz.vutbr.fit.openmrdp.model;

import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 01.03.2018.
 */
interface InformationBaseService {

    List<RDFTriple> loadInformationBase();

    void addInformationToBase(RDFTriple triple);
}
