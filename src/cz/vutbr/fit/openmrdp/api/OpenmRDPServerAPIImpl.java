package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.InformationBaseProdService;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class OpenmRDPServerAPIImpl implements OpenmRDPServerAPI{

    private final InfoManager infoManager;

    public OpenmRDPServerAPIImpl() {
        infoManager = new InfoManager(new InformationBaseProdService());
    }

    @Override
    public void receiveIncomingMessages() {

    }

    @Override
    public void addInformationToInformationModel(String subject, String predicate, String object) {
        infoManager.addInformationToBase(subject, predicate, object);
    }
}
