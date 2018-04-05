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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

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

    private static final String MATERIAL_VARIABLE_NAME = "?material";
    private static final String ROOM_VARIABLE_NAME = "?room";
    private static final String SURFACE_VARIABLE_NAME = "?sur";
    private static final String FUEL_RESOURCE_NAME = "urn:uuid:fuel1";
    private static final String BOX_RESOURCE_NAME = "urn:uuid:box1";
    private static final String SURFACE_RESOURCE_NAME = "urn:uuid:surface1";
    private static final String DRILL_RESOURCE_NAME = "urn:uuid:drill1";
    private static final String ROOM_RESOURCE_NAME = "urn:uuid:room1";

    private static final List<VariableResourcePair> EXPECTED_RESOURCE_PAIR_1 = new ArrayList<VariableResourcePair>() {{
        add(new VariableResourcePair(MATERIAL_VARIABLE_NAME, FUEL_RESOURCE_NAME));
        add(new VariableResourcePair(ROOM_VARIABLE_NAME, ROOM_RESOURCE_NAME));
        add(new VariableResourcePair(SURFACE_VARIABLE_NAME, SURFACE_RESOURCE_NAME));
    }};

    private static final List<VariableResourcePair> EXPECTED_RESOURCE_PAIR_2 = new ArrayList<VariableResourcePair>() {{
        add(new VariableResourcePair(MATERIAL_VARIABLE_NAME, BOX_RESOURCE_NAME));
        add(new VariableResourcePair(ROOM_VARIABLE_NAME, BOX_RESOURCE_NAME));
        add(new VariableResourcePair(SURFACE_VARIABLE_NAME, SURFACE_RESOURCE_NAME));
    }};

    private static final List<VariableResourcePair> EXPECTED_RESOURCE_PAIR_3 = new ArrayList<VariableResourcePair>() {{
        add(new VariableResourcePair(MATERIAL_VARIABLE_NAME, DRILL_RESOURCE_NAME));
        add(new VariableResourcePair(ROOM_VARIABLE_NAME, ROOM_RESOURCE_NAME));
        add(new VariableResourcePair(SURFACE_VARIABLE_NAME, SURFACE_RESOURCE_NAME));
    }};

    private static final List<VariableResourcePair> EXPECTED_RESOURCE_PAIR_4 = new ArrayList<VariableResourcePair>() {{
        add(new VariableResourcePair(MATERIAL_VARIABLE_NAME, FUEL_RESOURCE_NAME));
        add(new VariableResourcePair(ROOM_VARIABLE_NAME, BOX_RESOURCE_NAME));
        add(new VariableResourcePair(SURFACE_VARIABLE_NAME, SURFACE_RESOURCE_NAME));
    }};

    private static final List<VariableResourcePair> EXPECTED_RESOURCE_PAIR_5 = new ArrayList<VariableResourcePair>() {{
        add(new VariableResourcePair(MATERIAL_VARIABLE_NAME, BOX_RESOURCE_NAME));
        add(new VariableResourcePair(ROOM_VARIABLE_NAME, ROOM_RESOURCE_NAME));
        add(new VariableResourcePair(SURFACE_VARIABLE_NAME, SURFACE_RESOURCE_NAME));
    }};

    private static final List<VariableResourcePair> EXPECTED_RESOURCE_PAIR_6 = new ArrayList<VariableResourcePair>() {{
        add(new VariableResourcePair(MATERIAL_VARIABLE_NAME, DRILL_RESOURCE_NAME));
        add(new VariableResourcePair(ROOM_VARIABLE_NAME, BOX_RESOURCE_NAME));
        add(new VariableResourcePair(SURFACE_VARIABLE_NAME, SURFACE_RESOURCE_NAME));
    }};



    @Test
    public void findAllRelevantVariablesForQuery() {
        Query testQuery = createTestQuery();

        Set<QueryVariable> foundedVariables = QueryVariableProcessor.identifyVariables(testQuery.getQueryTriples());

        assertVariables(foundedVariables);
    }

    @Test
    public void getVariablePairByName() {
        VariableResourcePair variableResourcePair = QueryVariableProcessor.getVariablePairByVariableName(MATERIAL_VARIABLE_NAME, createTestVariableResourcePairs());

        assertThat(variableResourcePair.getVariableName(), is(MATERIAL_VARIABLE_NAME));
        assertThat(variableResourcePair.getResourceName(), is(FUEL_RESOURCE_NAME));
    }

    @Test
    public void prepareVariableCombinations() {
        List<List<VariableResourcePair>> variableResourcePairs = QueryVariableProcessor.prepareVariableCombinations(createTestQueryVariableSet());

        assertThat(variableResourcePairs, hasSize(6));
        assertThat(variableResourcePairs.contains(EXPECTED_RESOURCE_PAIR_1), is(true));
        assertThat(variableResourcePairs.contains(EXPECTED_RESOURCE_PAIR_2), is(true));
        assertThat(variableResourcePairs.contains(EXPECTED_RESOURCE_PAIR_3), is(true));
        assertThat(variableResourcePairs.contains(EXPECTED_RESOURCE_PAIR_4), is(true));
        assertThat(variableResourcePairs.contains(EXPECTED_RESOURCE_PAIR_5), is(true));
        assertThat(variableResourcePairs.contains(EXPECTED_RESOURCE_PAIR_6), is(true));
    }

    private Set<QueryVariable> createTestQueryVariableSet() {
        Set<QueryVariable> queryVariables = new HashSet<>();

        QueryVariable materialQueryVariable = createMaterialQueryVariable();
        queryVariables.add(materialQueryVariable);

        QueryVariable surfaceQueryVariable = createSurfaceQueryVariable();
        queryVariables.add(surfaceQueryVariable);

        QueryVariable roomQueryVariable = createRoomQueryVariable();
        queryVariables.add(roomQueryVariable);

        return queryVariables;

    }

    private QueryVariable createRoomQueryVariable() {
        QueryVariable roomQueryVariable = new QueryVariable(ROOM_VARIABLE_NAME);
        roomQueryVariable.getResourceName().add(ROOM_RESOURCE_NAME);
        roomQueryVariable.getResourceName().add(BOX_RESOURCE_NAME);
        return roomQueryVariable;
    }

    private QueryVariable createSurfaceQueryVariable() {
        QueryVariable surfaceQueryVariable = new QueryVariable(SURFACE_VARIABLE_NAME);
        surfaceQueryVariable.getResourceName().add(SURFACE_RESOURCE_NAME);
        return surfaceQueryVariable;
    }

    private QueryVariable createMaterialQueryVariable() {
        QueryVariable materialQueryVariable = new QueryVariable(MATERIAL_VARIABLE_NAME);
        materialQueryVariable.getResourceName().add(FUEL_RESOURCE_NAME);
        materialQueryVariable.getResourceName().add(BOX_RESOURCE_NAME);
        materialQueryVariable.getResourceName().add(DRILL_RESOURCE_NAME);
        return materialQueryVariable;
    }

    private void assertVariables(Set<QueryVariable> identifiedVariables) {
        assertThat(identifiedVariables, hasSize(3));
        QueryVariable roomVariable = new QueryVariable("?room");
        QueryVariable materialVariable = new QueryVariable(MATERIAL_VARIABLE_NAME);
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

    private List<VariableResourcePair> createTestVariableResourcePairs() {
        List<VariableResourcePair> variableResourcePairs = new ArrayList<>();

        variableResourcePairs.add(new VariableResourcePair(MATERIAL_VARIABLE_NAME, FUEL_RESOURCE_NAME));
        variableResourcePairs.add(new VariableResourcePair("?room", "urn:uuid:room1"));

        return variableResourcePairs;
    }
}