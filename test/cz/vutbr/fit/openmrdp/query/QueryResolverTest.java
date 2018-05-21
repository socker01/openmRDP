package cz.vutbr.fit.openmrdp.query;

import com.google.common.collect.Sets;
import cz.vutbr.fit.openmrdp.exceptions.QueryProcessingException;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyTestService;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 18.02.2018.
 */
public final class QueryResolverTest {

    private static final RDFTriple TEST_QUERY_TRIPLE_1 = new RDFTriple("?material", "loc:locatedIn", "?room");
    private static final RDFTriple TEST_QUERY_TRIPLE_2 = new RDFTriple("<urn:uuid:drill1>", "loc:locatedIn", "?room");
    private static final RDFTriple TEST_QUERY_TRIPLE_3 = new RDFTriple("?sur", "rdf:type", "mat:metallicThing");

    private static final String TEST_QUERY = "?material <loc:locatedIn> ?room" + "\n" +
            "?material rdf:type mat:inflammableThing" + "\n" +
            "urn:uuid:drill1 <loc:locatedIn> ?room" + "\n" +
            "urn:uuid:drill1 task:drilling ?sur" + "\n" +
            "?sur rdf:type mat:metallicThing";

    private static final RDFTriple TEST_QUERY_FACT = new RDFTriple("<urn:uuid:fuel1>", "loc:locatedIn", "<urn:uuid:room1>");

    private final InfoManager infoManager = InfoManager.getInfoManager(new InformationBaseTestService(), new OntologyTestService());
    private final QueryResolver queryResolver = new QueryResolver(infoManager);

    @Test
    public void findValidResourcesForObjectVariables() {
        Set<String> resources = queryResolver.findValidResourcesForObjectVariable(Sets.newHashSet(TEST_QUERY_TRIPLE_3));

        assertThat(resources, containsInAnyOrder("mat:metallicThing"));
    }

    @Test
    public void findValidResourcesForSubjectVariables() {
        Set<String> resources = queryResolver.findValidResourcesForSubjectVariable(Sets.newHashSet(TEST_QUERY_TRIPLE_2));

        assertThat(resources, containsInAnyOrder("<urn:uuid:drill1>"));
    }

    @Test
    public void verifyFact() {
        Set<RDFTriple> matchingPatterns = Sets.newHashSet(TEST_QUERY_FACT);

        assertThat(queryResolver.verifyFact(TEST_QUERY_FACT, matchingPatterns), is(true));

        matchingPatterns.add(TEST_QUERY_TRIPLE_1);
        assertThat(queryResolver.verifyFact(TEST_QUERY_FACT, matchingPatterns), is(false));

        matchingPatterns = Sets.newHashSet(TEST_QUERY_TRIPLE_1);
        assertThat(queryResolver.verifyFact(TEST_QUERY_FACT, matchingPatterns), is(false));
    }

    @Test
    public void resolveQuery() {
        MessageBody messageBody = new MessageBody(TEST_QUERY, ContentType.PLANT_QUERY);
        List<String> foundResources = queryResolver.resolveQuery(messageBody, "?material");

        assertThat(foundResources, hasSize(1));
        assertThat(foundResources, containsInAnyOrder("urn:uuid:fuel1"));
    }

    @Test(expected = QueryProcessingException.class)
    public void resolveQueryForNonExistingResource() {
        MessageBody messageBody = new MessageBody(TEST_QUERY, ContentType.PLANT_QUERY);
        queryResolver.resolveQuery(messageBody, "?nonExists");
    }
}