package cz.vutbr.fit.openmrdp.model;

import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
final class QueryProcessorTest {

    private static final ContentType TEST_CONTENT_TYPE = ContentType.PLANT_QUERY;
    private static final String TEST_PREDICATE = "<http://www.test.com/test/locatedIn>";
    private static final String TEST_ROOM_VARIABLE = "?room";
    private static final String TEST_BUILDING_VARIABLE = "?building";
    private static final String TEST_CITY = "testCity";

    @Test
    void testCreateQueryFromMessage() throws QuerySyntaxException {
        MessageBody messageBody = createMessageBody();

        Query query = QueryProcessor.processQuery(messageBody);

        assertThat(query.getQueryTriples(), hasSize(2));
        assertFirstTriple(query.getQueryTriples().get(0));
        assertSecondTriple(query.getQueryTriples().get(1));
        assertThat(query.getQueryType(), is(TEST_CONTENT_TYPE));
    }

    @Test
    void testIncorrectQueryFromMessage(){
        MessageBody messageBody = createMessageBodyWithIncorrectQuery();

        assertThrows(QuerySyntaxException.class, () -> QueryProcessor.processQuery(messageBody));
    }

    private void assertFirstTriple(RDFTriple rdfTriple) {
        assertThat(rdfTriple.getSubject(), is("?room"));
        assertThat(rdfTriple.getPredicate(), is(TEST_PREDICATE));
        assertThat(rdfTriple.getObject(), is("?building"));
    }

    private void assertSecondTriple(RDFTriple rdfTriple) {
        assertThat(rdfTriple.getSubject(), is("?building"));
        assertThat(rdfTriple.getPredicate(), is(TEST_PREDICATE));
        assertThat(rdfTriple.getObject(), is("testCity"));
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