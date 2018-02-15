package cz.vutbr.fit.openmrdp.model;

import cz.vutbr.fit.openmrdp.messages.ContentType;

import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class Query {

    private List<RDFTriple> queryTriples;
    private final ContentType queryType;

    public Query(List<RDFTriple> queryTriples, ContentType queryType) {
        this.queryTriples = queryTriples;
        this.queryType = queryType;
    }

    public List<RDFTriple> getQueryTriples() {
        return queryTriples;
    }

    public ContentType getQueryType() {
        return queryType;
    }
}
