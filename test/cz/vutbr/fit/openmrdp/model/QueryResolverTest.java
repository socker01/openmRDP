package cz.vutbr.fit.openmrdp.model;

import cz.vutbr.fit.openmrdp.messages.ContentType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 18.02.2018.
 */
final class QueryResolverTest {

    private QueryResolver queryResolver = new QueryResolver();

    @Test
    void testFindAllRelevantPatternsForQuery(){
        Query testQuery = createTestQuery();

        queryResolver.resolveQuery(testQuery);

        assertVariables();
        assertMatchingPatterns();
    }

    private Query createTestQuery(){
        List<RDFTriple> triples = initializeTestRDPTriples();

        return new Query(triples, ContentType.PLANT_QUERY);
    }

    private List<RDFTriple> initializeTestRDPTriples(){
        List<RDFTriple> triples = new ArrayList<>();
        triples.add(InformationBaseTestService.TEST_TRIPLE_1);
        triples.add(InformationBaseTestService.TEST_TRIPLE_2);
        triples.add(InformationBaseTestService.TEST_TRIPLE_3);

        return triples;
    }

    private void assertVariables(){
        assertThat(queryResolver.getVariables(), hasSize(2));
        assertThat(queryResolver.getVariables(), containsInAnyOrder("?room", "?building"));
    }

    private void assertMatchingPatterns(){
        assertThat(queryResolver.getMatchingPatterns(), hasSize(2));
        assertThat(queryResolver.getMatchingPatterns(), containsInAnyOrder(InformationBaseTestService.TEST_TRIPLE_1, InformationBaseTestService.TEST_TRIPLE_3));
    }
}