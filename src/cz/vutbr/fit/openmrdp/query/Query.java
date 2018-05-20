package cz.vutbr.fit.openmrdp.query;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;

import java.util.Set;

/**
 * Internal domain object which contains parsed data about query.
 *
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
final class Query {

    @NotNull
    private Set<RDFTriple> queryTriples;
    @NotNull
    private final ContentType queryType;

    Query(@NotNull Set<RDFTriple> queryTriples, @NotNull ContentType queryType) {
        this.queryTriples = Preconditions.checkNotNull(queryTriples);
        this.queryType = Preconditions.checkNotNull(queryType);
    }

    @NotNull
    Set<RDFTriple> getQueryTriples() {
        return queryTriples;
    }

    @NotNull
    ContentType getQueryType() {
        return queryType;
    }
}
