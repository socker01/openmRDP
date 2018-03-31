package cz.vutbr.fit.openmrdp.query;

import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.model.RDFTriple;

import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
final class Query {

    private Set<RDFTriple> queryTriples;
    private final ContentType queryType;

    Query(Set<RDFTriple> queryTriples, ContentType queryType) {
        this.queryTriples = queryTriples;
        this.queryType = queryType;
    }

    Set<RDFTriple> getQueryTriples() {
        return queryTriples;
    }

    ContentType getQueryType() {
        return queryType;
    }
}
