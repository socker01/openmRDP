package cz.vutbr.fit.openmrdp.model;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
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
        infoManager.createInformationBase();
    }

    @Test
    void testFindMatchingPatternsForRoomVariable(){
        Set<RDFTriple> matchingPatterns = infoManager.findAllMatchingPatterns(Sets.newHashSet("?room"));
        assertThat(matchingPatterns, hasSize(2));
        assertThat(matchingPatterns, containsInAnyOrder(InformationBaseTestService.TEST_TRIPLE_1, InformationBaseTestService.TEST_TRIPLE_3));
    }

    @Test
    void testFindNonExistingMatchingPattern(){
        Set<RDFTriple> matchingPatterns = infoManager.findAllMatchingPatterns(Sets.newHashSet("?swimmingPool"));
        assertThat(matchingPatterns, hasSize(0));
    }

    @Test
    void testAddInformationToInformationModel(){
        infoManager.addInformationToBase(InformationBaseTestService.TEST_TRIPLE_6);
        //TODO: test new information is in infoManager info list and in infoService as well.
    }
}