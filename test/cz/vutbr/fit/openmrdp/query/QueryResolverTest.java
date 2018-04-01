package cz.vutbr.fit.openmrdp.query;

import com.google.common.collect.Sets;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.base.QueryVariable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 18.02.2018.
 */
public final class QueryResolverTest {

    private static final RDFTriple TEST_QUERY_TRIPLE_1 = new RDFTriple("?material", "loc:locatedIn", "?room");
    private static final RDFTriple TEST_QUERY_TRIPLE_2 = new RDFTriple("?material", "rdf:type", "mat:inflammableThing");
    private static final RDFTriple TEST_QUERY_TRIPLE_3 = new RDFTriple("<urn:uuid:drill1>", "loc:locatedIn", "?room");
    private static final RDFTriple TEST_QUERY_TRIPLE_4 = new RDFTriple("<urn:uuid:drill1>", "task:drilling", "?sur");
    private static final RDFTriple TEST_QUERY_TRIPLE_5 = new RDFTriple("?sur", "rdf:type", "mat:metallicThing");

    private static final RDFTriple TEST_QUERY_FACT = new RDFTriple("<urn:uuid:fuel1>", "loc:locatedIn", "<urn:uuid:room1>");

    private final InfoManager infoManager = new InfoManager(new InformationBaseTestService());
    private final QueryResolver queryResolver = new QueryResolver(infoManager);

    @Test
    public void findAllRelevantVariablesForQuery() {
        Query testQuery = createTestQuery();

        Set<QueryVariable> foundedVariables = queryResolver.identifyVariables(testQuery.getQueryTriples());

        assertVariables(foundedVariables);
    }

    @Test
    public void findValidResourcesForObjectVariables() {
        Set<String> resources = queryResolver.findValidResourcesForObjectVariable(Sets.newHashSet(TEST_QUERY_TRIPLE_5));

        assertThat(resources, containsInAnyOrder("mat:metallicThing"));
    }

    @Test
    public void findValidResourcesForSubjectVariables() {
        Set<String> resources = queryResolver.findValidResourcesForSubjectVariable(Sets.newHashSet(TEST_QUERY_TRIPLE_3));

        assertThat(resources, containsInAnyOrder("<urn:uuid:drill1>"));
    }

    @Test
    public void verifyFact(){
        Set<RDFTriple> matchingPatterns = Sets.newHashSet(TEST_QUERY_FACT);

        assertThat(queryResolver.verifyFact(TEST_QUERY_FACT, matchingPatterns), is(true));

        matchingPatterns.add(TEST_QUERY_TRIPLE_1);
        assertThat(queryResolver.verifyFact(TEST_QUERY_FACT, matchingPatterns), is(false));

        matchingPatterns = Sets.newHashSet(TEST_QUERY_TRIPLE_1);
        assertThat(queryResolver.verifyFact(TEST_QUERY_FACT, matchingPatterns), is(false));
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

    private void assertVariables(Set<QueryVariable> identifiedVariables) {
        assertThat(identifiedVariables, hasSize(3));
        QueryVariable roomVariable = new QueryVariable("?room");
        QueryVariable materialVariable = new QueryVariable("?material");
        QueryVariable surVariable = new QueryVariable("?sur");
        assertThat(identifiedVariables, containsInAnyOrder(roomVariable, materialVariable, surVariable));
    }

}