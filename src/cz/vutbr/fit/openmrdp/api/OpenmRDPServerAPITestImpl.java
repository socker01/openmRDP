package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.InformationBaseTestService;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPServerAPITestImpl implements OpenmRDPServerAPI{

    private final InfoManager infoManager;

    public OpenmRDPServerAPITestImpl() {
        infoManager = new InfoManager(new InformationBaseTestService());
    }

    @Override
    public void receiveIncomingMessages() {

    }

    @Override
    public void addInformationToInformationModel(String subject, String predicate, String object) {
        infoManager.addInformationToBase(subject, predicate, object);
    }
}
