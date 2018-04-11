package cz.vutbr.fit.openmrdp.model.informationbase;

import cz.vutbr.fit.openmrdp.model.ontology.OntologyTestService;
import org.junit.Test;

public final class InformationBaseCreatorTest {

    @Test
    public void testCreateTransitivePredicateTree(){
        InformationBaseCreator informationBaseCreator = new InformationBaseCreator(new InformationBaseTestService(), new OntologyTestService());

        informationBaseCreator.createInformationBase();
    }

}