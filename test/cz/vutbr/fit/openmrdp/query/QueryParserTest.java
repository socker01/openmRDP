package cz.vutbr.fit.openmrdp.query;

import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public final class QueryParserTest {

    private static final String TEST_VARIABLE = "?material";
    private static final String TEST_CONDITION = "?material <loc:locatedIn> urn:uuid:room1";
    private static final String VALID_QUERY = "IDENTIFY " + TEST_VARIABLE + " WHERE " + TEST_CONDITION;
    private static final String INVALID_QUERY = TEST_VARIABLE + " WHERE " + TEST_CONDITION;

    @Test
    public void parseValidQuery() throws QuerySyntaxException {
        QueryRaw queryRaw = QueryParser.parseQuery(VALID_QUERY);

        assertThat(queryRaw.getResourceName(), is(TEST_VARIABLE));
        assertThat(queryRaw.getConditions(), is(TEST_CONDITION));
    }

    @Test(expected = QuerySyntaxException.class)
    public void parseInvalidQuery() throws QuerySyntaxException {
        QueryParser.parseQuery(INVALID_QUERY);
    }
}