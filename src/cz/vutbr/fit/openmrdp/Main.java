package cz.vutbr.fit.openmrdp;

import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.InformationBaseTestService;

public class Main {

    public static void main(String[] args) {

        InfoManager manager = new InfoManager(new InformationBaseTestService());
        manager.createInformationBase();

        System.out.println("test");
    }
}
