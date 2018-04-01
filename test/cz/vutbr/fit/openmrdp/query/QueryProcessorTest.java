package cz.vutbr.fit.openmrdp.query;

import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class QueryProcessorTest {

    private static final ContentType TEST_CONTENT_TYPE = ContentType.PLANT_QUERY;
    private static final String TEST_PREDICATE = "<http://www.test.com/test/locatedIn>";
    private static final String TEST_ROOM_VARIABLE = "?room";
    private static final String TEST_BUILDING_VARIABLE = "?building";
    private static final String TEST_CITY = "testCity";

    @Test
    public void testCreateQueryFromMessage() throws QuerySyntaxException {
        MessageBody messageBody = createMessageBody();

        Query query = QueryProcessor.processQuery(messageBody);

        assertThat(query.getQueryTriples(), hasSize(2));
        assertExpectedTriples(query.getQueryTriples());
        assertThat(query.getQueryType(), is(TEST_CONTENT_TYPE));
    }

    @Test(expected = QuerySyntaxException.class)
    public void testIncorrectQueryFromMessage() throws QuerySyntaxException {
        MessageBody messageBody = createMessageBodyWithIncorrectQuery();

        QueryProcessor.processQuery(messageBody);
    }

    private void assertExpectedTriples(Set<RDFTriple> rdfTriples) {
        RDFTriple expectedTriple1 = new RDFTriple("?room", TEST_PREDICATE, "?building");
        RDFTriple expectedTriple2 = new RDFTriple("?building", TEST_PREDICATE, "testCity");

        assertThat(rdfTriples, hasItem(expectedTriple1));
        assertThat(rdfTriples, hasItem(expectedTriple2));
    }

    private MessageBody createMessageBody(){
        String query = TEST_ROOM_VARIABLE + " " + TEST_PREDICATE + " " + TEST_BUILDING_VARIABLE +
                "\n" + TEST_BUILDING_VARIABLE + " " + TEST_PREDICATE + " " + TEST_CITY;

        return new MessageBody(query, TEST_CONTENT_TYPE);
    }

    private MessageBody createMessageBodyWithIncorrectQuery() {
        String query = TEST_ROOM_VARIABLE + " " + TEST_BUILDING_VARIABLE;

        return new MessageBody(query, TEST_CONTENT_TYPE);
    }

}