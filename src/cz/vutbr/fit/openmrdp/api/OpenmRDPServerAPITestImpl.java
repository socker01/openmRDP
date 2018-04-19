package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyTestService;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPServerAPITestImpl implements OpenmRDPServerAPI{

    private final InfoManager infoManager;

    public OpenmRDPServerAPITestImpl() {
        infoManager = InfoManager.getInfoManager(new InformationBaseTestService(), new OntologyTestService());
    }

    @Override
    public void receiveMessages() {

    }

    @Override
    public void addInformationToInformationBase(RDFTriple information) {
        //TODO implement me
    }

    @Override
    public void removeInformationFromInformationBase(RDFTriple information) {
        //TODO implement me
    }
}
