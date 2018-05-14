package cz.vutbr.fit.openmrdp.model;

import com.google.common.collect.Sets;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyTestService;
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

    private static final RDFTriple TEST_TRIPLE_1 = new RDFTriple("urn:uuid:drill1", "<loc:locatedIn>", "urn:uuid:room1");
    private static final RDFTriple TEST_TRIPLE_2 = new RDFTriple("urn:uuid:box1", "<loc:locatedIn>", "urn:uuid:room1");
    private static final RDFTriple TEST_TRIPLE_3 = new RDFTriple("urn:uuid:fuel1", "<loc:locatedIn>", "urn:uuid:box1");
    private static final RDFTriple TEST_TRIPLE_4 = new RDFTriple("urn:uuid:fuel1", "<loc:locatedIn>", "urn:uuid:room1");
    private static final RDFTriple TEST_TRIPLE_TO_ADD = new RDFTriple("urn:uuid:fuel1", "<loc:contains>", "urn:uuid:test");
    private static final RDFTriple SYMMETRICAL_TEST_TRIPLE = new RDFTriple("urn:uuid:test", "<loc:locatedIn>", "urn:uuid:fuel1");

    private InfoManager infoManager;

    @Before
    public void setUp(){
        infoManager = InfoManager.getInfoManager(new InformationBaseTestService(), new OntologyTestService());
    }

    @Test
    public void findMatchingPatternsForRoomVariable(){
        Set<RDFTriple> matchingPatterns = infoManager.findMatchingPatterns(new RDFTriple("urn:uuid:drill1", "<loc:locatedIn>", "?room"));
        assertThat(matchingPatterns, hasSize(1));
        assertThat(matchingPatterns, containsInAnyOrder(TEST_TRIPLE_1));
    }

    @Test
    public void findMatchingPatternsForTwoVariables(){
        Set<RDFTriple> matchingPattern = infoManager.findMatchingPatterns(new RDFTriple("?item", "<loc:locatedIn>", "?room"));

        assertThat(matchingPattern, hasSize(4));
        assertThat(matchingPattern, containsInAnyOrder(TEST_TRIPLE_1,
                TEST_TRIPLE_2,
                TEST_TRIPLE_3,
                TEST_TRIPLE_4));
    }

    @Test
    public void findNonExistingMatchingPattern(){
        Set<RDFTriple> matchingPatterns = infoManager.findMatchingPatterns(new RDFTriple("urn:uuid:drill1", "<loc:has>", "?room"));
        assertThat(matchingPatterns, hasSize(0));
    }

    @Test
    public void verifyFact(){
        assertThat(infoManager.verifyFacts(Sets.newHashSet(TEST_TRIPLE_1)), is(true));

        RDFTriple nonExistingTriple = new RDFTriple("aaa", "bbb", "ccc");
        assertThat(infoManager.verifyFacts(Sets.newHashSet(nonExistingTriple)), is(false));
    }

    @Test
    public void addInformationToInformationModel(){
        infoManager.addInformationToBase(TEST_TRIPLE_TO_ADD);

        assertThat(infoManager.findResourceLocation("urn:uuid:test"), is("urn:uuid:room1/urn:uuid:box1/urn:uuid:fuel1/urn:uuid:test"));

        Set<RDFTriple> matchingPattern = infoManager.findMatchingPatterns(new RDFTriple("?item", "<loc:locatedIn>", "?room"));

        assertThat(matchingPattern, hasSize(5));
        assertThat(matchingPattern, containsInAnyOrder(TEST_TRIPLE_1,
                TEST_TRIPLE_2,
                TEST_TRIPLE_3,
                TEST_TRIPLE_4,
                SYMMETRICAL_TEST_TRIPLE));

        infoManager.removeInformationFromBase(TEST_TRIPLE_TO_ADD);
    }
}