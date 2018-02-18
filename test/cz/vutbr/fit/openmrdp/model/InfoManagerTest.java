package cz.vutbr.fit.openmrdp.model;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
final class InfoManagerTest {

    static final RDFTriple TEST_TRIPLE_1 = new RDFTriple("?room", "<name>", "serviceRoom");
    static final RDFTriple TEST_TRIPLE_2 = new RDFTriple("serviceRoom", "<contains>", "broom");
    static final RDFTriple TEST_TRIPLE_3 = new RDFTriple("?room", "<locatedIn>", "?building");

    @Test
    void testFindMatchingPatternsForRoomVariable(){
        InfoManager.createInformationBase(createTestInformationBase());

        Set<RDFTriple> matchingPatterns = InfoManager.findAllMatchingPatterns(Sets.newHashSet("?room"));
        assertThat(matchingPatterns, hasSize(2));
        assertThat(matchingPatterns, containsInAnyOrder(TEST_TRIPLE_1, TEST_TRIPLE_3));
    }

    @Test
    void testFindNonExistingMatchingPattern(){
        InfoManager.createInformationBase(createTestInformationBase());

        Set<RDFTriple> matchingPatterns = InfoManager.findAllMatchingPatterns(Sets.newHashSet("?swimmingPool"));
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