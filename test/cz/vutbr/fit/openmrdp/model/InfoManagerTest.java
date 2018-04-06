package cz.vutbr.fit.openmrdp.model;

import com.google.common.collect.Sets;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
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
    private static final RDFTriple TEST_TRIPLE_5 = new RDFTriple("fuel:chemicalFuel", "rdf:subtype", "mat:inflammableThing");

    private InfoManager infoManager;

    @Before
    public void setUp(){
        infoManager = new InfoManager(new InformationBaseTestService());
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
        infoManager.addInformationToBase(TEST_TRIPLE_5.getSubject(),
                TEST_TRIPLE_5.getPredicate(),
                TEST_TRIPLE_5.getObject()
        );
        //TODO: test new information is in infoManager info list and in infoService as well.
    }
}