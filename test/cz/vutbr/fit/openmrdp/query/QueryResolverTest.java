package cz.vutbr.fit.openmrdp.query;

import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.RDFTriple;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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

    private final InfoManager infoManager = new InfoManager(new InformationBaseTestService());
    private final QueryResolver queryResolver = new QueryResolver(infoManager);

    @Test
    public void testFindAllRelevantVariablesForQuery(){
        Query testQuery = createTestQuery();

        Set<String> foundedVariables = queryResolver.identifyVariables(testQuery.getQueryTriples());

        assertVariables(foundedVariables);
    }

    private Query createTestQuery(){
        Set<RDFTriple> triples = initializeTestRDPTriples();

        return new Query(triples, ContentType.PLANT_QUERY);
    }

    private Set<RDFTriple> initializeTestRDPTriples(){
        Set<RDFTriple> triples = new HashSet<>();
        triples.add(TEST_QUERY_TRIPLE_1);
        triples.add(TEST_QUERY_TRIPLE_2);
        triples.add(TEST_QUERY_TRIPLE_3);
        triples.add(TEST_QUERY_TRIPLE_4);
        triples.add(TEST_QUERY_TRIPLE_5);

        return triples;
    }

    private void assertVariables(Set<String> identifiedVariables){
        assertThat(identifiedVariables, hasSize(3));
        assertThat(identifiedVariables, containsInAnyOrder("?room", "?material", "?sur"));
    }

}