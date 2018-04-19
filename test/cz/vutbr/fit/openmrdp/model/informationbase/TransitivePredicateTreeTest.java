package cz.vutbr.fit.openmrdp.model.informationbase;

import cz.vutbr.fit.openmrdp.model.ontology.OntologyService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyTestService;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

/**
 * @author Jiri Koudelka
 * @since 12.04.2018
 */
public final class TransitivePredicateTreeTest {

    @Test
    public void createTransitivePredicatesList() {
        OntologyService ontologyService = new OntologyTestService();
        TransitivePredicateTree predicateTree = new TransitivePredicateTree(ontologyService.loadOntology().getTransitivePredicates());

        List<List<String>> transitivePredicatesList = predicateTree.getTransitivePredicatesList();

        assertThat(transitivePredicatesList, hasSize(2));

        if (transitivePredicatesList.get(0).size() == 2) {
            assertThat(transitivePredicatesList.get(0), containsInAnyOrder("rdf:type", "rdf:has"));
            assertThat(transitivePredicatesList.get(1), hasSize(3));
            assertThat(transitivePredicatesList.get(1), containsInAnyOrder("rdf:type", "rdf:subtype", "rdf:subsubtype"));
        } else {
            assertThat(transitivePredicatesList.get(0), hasSize(3));
            assertThat(transitivePredicatesList.get(0), containsInAnyOrder("rdf:type", "rdf:subtype", "rdf:subsubtype"));
            assertThat(transitivePredicatesList.get(1), hasSize(2));
            assertThat(transitivePredicatesList.get(1), containsInAnyOrder("rdf:type", "rdf:has"));
        }
    }
}