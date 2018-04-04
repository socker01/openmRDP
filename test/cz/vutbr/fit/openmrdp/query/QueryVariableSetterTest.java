package cz.vutbr.fit.openmrdp.query;

import com.google.common.collect.Sets;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.base.VariableResourcePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 04.04.2018.
 */
public final class QueryVariableSetterTest {

    private static final RDFTriple TEST_TRIPLE_1 = new RDFTriple("?material", "loc:locatedIn", "urn:uuid:room1");
    private static final RDFTriple TEST_TRIPLE_2 = new RDFTriple("urn:uuid:room1", "loc:contains", "?item");

    private static final RDFTriple EXPECTED_TRIPLE_1 = new RDFTriple("urn:uuid:fuel1", "loc:locatedIn", "urn:uuid:room1");
    private static final RDFTriple EXPECTED_TRIPLE_2 = new RDFTriple("urn:uuid:room1", "loc:contains", "urn:uuid:box1");

    @Test
    public void substituteQueryVariables(){
        Query query = new Query(Sets.newHashSet(TEST_TRIPLE_1, TEST_TRIPLE_2), ContentType.PLANT_QUERY);
        QueryVariableSetter queryVariableSetter = new QueryVariableSetter(query);

        List<VariableResourcePair> variableResourcePairs = createVariableResourcePair();

        Set<RDFTriple> substitutedTriples = queryVariableSetter.substituteVariableValues(variableResourcePairs);

        assertThat(substitutedTriples, hasSize(2));
        assertThat(substitutedTriples, containsInAnyOrder(EXPECTED_TRIPLE_1, EXPECTED_TRIPLE_2));
    }

    private List<VariableResourcePair> createVariableResourcePair(){
        List<VariableResourcePair> variableResourcePairs = new ArrayList<>();
        variableResourcePairs.add(new VariableResourcePair("?material", "urn:uuid:fuel1"));
        variableResourcePairs.add(new VariableResourcePair("?item", "urn:uuid:box1"));

        return variableResourcePairs;
    }

}