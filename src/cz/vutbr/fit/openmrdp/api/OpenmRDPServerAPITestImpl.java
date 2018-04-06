package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;

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
}
