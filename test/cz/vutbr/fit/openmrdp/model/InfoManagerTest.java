package cz.vutbr.fit.openmrdp.model;

import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class InfoManagerTest {

    private InfoManager infoManager;

    @Before
    public void setUp(){
        infoManager = new InfoManager(new InformationBaseTestService());
    }

    @Test
    public void findMatchingPatternsForRoomVariable(){
        Set<RDFTriple> matchingPatterns = infoManager.findMatchingPatterns(new RDFTriple("urn:uuid:drill1", "<loc:locatedIn>", "?room"));
        assertThat(matchingPatterns, hasSize(1));
        assertThat(matchingPatterns, containsInAnyOrder(InformationBaseTestService.TEST_TRIPLE_1));
    }

    @Test
    public void findMatchingPatternsForTwoVariables(){
        Set<RDFTriple> matchingPattern = infoManager.findMatchingPatterns(new RDFTriple("?item", "<loc:locatedIn>", "?room"));

        assertThat(matchingPattern, hasSize(4));
        assertThat(matchingPattern, containsInAnyOrder(InformationBaseTestService.TEST_TRIPLE_1,
                InformationBaseTestService.TEST_TRIPLE_3,
                InformationBaseTestService.TEST_TRIPLE_4,
                InformationBaseTestService.TEST_TRIPLE_5));
    }

    @Test
    public void findNonExistingMatchingPattern(){
        Set<RDFTriple> matchingPatterns = infoManager.findMatchingPatterns(new RDFTriple("urn:uuid:drill1", "<loc:has>", "?room"));
        assertThat(matchingPatterns, hasSize(0));
    }

    @Test
    public void verifyFact(){
        assertThat(infoManager.verifyFact(InformationBaseTestService.TEST_TRIPLE_1), is(true));

        RDFTriple nonExistingTriple = new RDFTriple("aaa", "bbb", "ccc");
        assertThat(infoManager.verifyFact(nonExistingTriple), is(false));
    }

    @Test
    public void addInformationToInformationModel(){
        infoManager.addInformationToBase(InformationBaseTestService.TEST_TRIPLE_13.getSubject(),
                InformationBaseTestService.TEST_TRIPLE_13.getPredicate(),
                InformationBaseTestService.TEST_TRIPLE_13.getObject()
        );
        //TODO: test new information is in infoManager info list and in infoService as well.
    }
}