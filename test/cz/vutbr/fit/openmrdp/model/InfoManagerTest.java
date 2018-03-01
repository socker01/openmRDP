package cz.vutbr.fit.openmrdp.model;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
final class InfoManagerTest {

    private InfoManager infoManager;

    @BeforeEach
    void setUp(){
        infoManager = new InfoManager(new InformationBaseTestService());
    }

    @Test
    void testFindMatchingPatternsForRoomVariable(){
        infoManager.createInformationBase();

        Set<RDFTriple> matchingPatterns = infoManager.findAllMatchingPatterns(Sets.newHashSet("?room"));
        assertThat(matchingPatterns, hasSize(2));
        assertThat(matchingPatterns, containsInAnyOrder(InformationBaseTestService.TEST_TRIPLE_1, InformationBaseTestService.TEST_TRIPLE_3));
    }

    @Test
    void testFindNonExistingMatchingPattern(){
        infoManager.createInformationBase();

        Set<RDFTriple> matchingPatterns = infoManager.findAllMatchingPatterns(Sets.newHashSet("?swimmingPool"));
        assertThat(matchingPatterns, hasSize(0));
    }
}