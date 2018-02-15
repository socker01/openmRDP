package cz.vutbr.fit.openmrdp.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
final class InfoManagerTest {

    private static final RDFTriple TEST_TRIPLE_1 = new RDFTriple("?room", "<name>", "serviceRoom");
    private static final RDFTriple TEST_TRIPLE_2 = new RDFTriple("serviceRoom", "<contains>", "broom");
    private static final RDFTriple TEST_TRIPLE_3 = new RDFTriple("?room", "<locatedIn>", "?Building");

    @Test
    void testFindMatchingPatternsForRoomVariable(){
        InfoManager.createInformationBase(createTestInformationBase());

        List<RDFTriple> matchingPatterns = InfoManager.findAllMatchingPatterns(Collections.singletonList("?room"));
        assertThat(matchingPatterns, hasSize(2));
        assertThat(matchingPatterns, containsInAnyOrder(TEST_TRIPLE_1, TEST_TRIPLE_3));
    }

    @Test
    void testFindNonExistingMatchingPattern(){
        InfoManager.createInformationBase(createTestInformationBase());

        List<RDFTriple> matchingPatterns = InfoManager.findAllMatchingPatterns(Collections.singletonList("?swimmingPool"));
        assertThat(matchingPatterns, hasSize(0));
    }

    private List<RDFTriple> createTestInformationBase(){
        List<RDFTriple> testInformationBase = new ArrayList<>();

        testInformationBase.add(TEST_TRIPLE_1);
        testInformationBase.add(TEST_TRIPLE_2);
        testInformationBase.add(TEST_TRIPLE_3);

        return testInformationBase;
    }
}