package cz.vutbr.fit.openmrdp.model.informationbase;

import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyTestService;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 12.04.2018
 */
public final class InformationBaseCreatorTest {

    @Test
    public void testCreateTransitivePredicateTree() {
        InformationBaseCreator informationBaseCreator = new InformationBaseCreator(new InformationBaseTestService(), new OntologyTestService());

        Set<RDFTriple> informationBase = informationBaseCreator.createInformationBase();

        assertThat(informationBase, hasSize(15));

        RDFTriple informationCreatedFromTransitiveRelation = new RDFTriple("urn:uuid:surface1", "rdf:type", "mat:metallicThing");
        assertThat(informationBase.contains(informationCreatedFromTransitiveRelation), is(true));
    }

}