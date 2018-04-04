package cz.vutbr.fit.openmrdp.query;

import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.model.base.QueryVariable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.base.VariableResourcePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Jiri Koudelka
 * @since 05.04.2018.
 */
public final class QueryVariableProcessorTest {

    private static final RDFTriple TEST_QUERY_TRIPLE_1 = new RDFTriple("?material", "loc:locatedIn", "?room");
    private static final RDFTriple TEST_QUERY_TRIPLE_2 = new RDFTriple("?material", "rdf:type", "mat:inflammableThing");
    private static final RDFTriple TEST_QUERY_TRIPLE_3 = new RDFTriple("<urn:uuid:drill1>", "loc:locatedIn", "?room");
    private static final RDFTriple TEST_QUERY_TRIPLE_4 = new RDFTriple("<urn:uuid:drill1>", "task:drilling", "?sur");
    private static final RDFTriple TEST_QUERY_TRIPLE_5 = new RDFTriple("?sur", "rdf:type", "mat:metallicThing");

    private static final String TEST_VARIABLE_NAME = "?material";
    private static final String TEST_RESOURCE_NAME = "urn:uuid:fuel1";

    @Test
    public void findAllRelevantVariablesForQuery() {
        Query testQuery = createTestQuery();

        Set<QueryVariable> foundedVariables = QueryVariableProcessor.identifyVariables(testQuery.getQueryTriples());

        assertVariables(foundedVariables);
    }

    @Test
    public void getVariablePairByName(){
        VariableResourcePair variableResourcePair = QueryVariableProcessor.getVariablePairByVariableName(TEST_VARIABLE_NAME, createTestVariableResourcePairs());

        assertThat(variableResourcePair.getVariableName(), is(TEST_VARIABLE_NAME));
        assertThat(variableResourcePair.getResourceName(), is(TEST_RESOURCE_NAME));
    }

    private void assertVariables(Set<QueryVariable> identifiedVariables) {
        assertThat(identifiedVariables, hasSize(3));
        QueryVariable roomVariable = new QueryVariable("?room");
        QueryVariable materialVariable = new QueryVariable(TEST_VARIABLE_NAME);
        QueryVariable surVariable = new QueryVariable("?sur");
        assertThat(identifiedVariables, containsInAnyOrder(roomVariable, materialVariable, surVariable));
    }

    private Query createTestQuery() {
        Set<RDFTriple> triples = initializeTestRDPTriples();

        return new Query(triples, ContentType.PLANT_QUERY);
    }

    private Set<RDFTriple> initializeTestRDPTriples() {
        Set<RDFTriple> triples = new HashSet<>();
        triples.add(TEST_QUERY_TRIPLE_1);
        triples.add(TEST_QUERY_TRIPLE_2);
        triples.add(TEST_QUERY_TRIPLE_3);
        triples.add(TEST_QUERY_TRIPLE_4);
        triples.add(TEST_QUERY_TRIPLE_5);

        return triples;
    }

    private List<VariableResourcePair> createTestVariableResourcePairs(){
        List<VariableResourcePair> variableResourcePairs = new ArrayList<>();

        variableResourcePairs.add(new VariableResourcePair(TEST_VARIABLE_NAME, TEST_RESOURCE_NAME));
        variableResourcePairs.add(new VariableResourcePair("?room", "urn:uuid:room1"));

        return variableResourcePairs;
    }
}