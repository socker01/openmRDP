package cz.vutbr.fit.openmrdp.query;

import com.google.common.annotations.VisibleForTesting;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.RDFTriple;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class QueryResolver {

    private final InfoManager infoManager;

    public QueryResolver(InfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @Nullable
    public String resolveQuery(MessageBody messageBody, String resourceToSearchName) {

        Query query;
        try {
            query = QueryProcessor.processQuery(messageBody);
        } catch (QuerySyntaxException e) {
            return null;
        }

        Set<String> variables = identifyVariables(query.getQueryTriples());
        Set<RDFTriple> matchingPatterns = findAllMatchingPatterns(variables);

        return "";
    }

    @VisibleForTesting
    Set<String> identifyVariables(Set<RDFTriple> triples) {
        Set<String> variables = new HashSet<>();
        for (RDFTriple triple : triples) {
            variables.addAll(findVariable(triple));
        }

        return variables;
    }

    private Set<String> findVariable(RDFTriple triple) {
        Set<String> variables = new HashSet<>();

        if (triple.getSubject().startsWith("?")) {
            variables.add(triple.getSubject());
        }

        if (triple.getObject().startsWith("?")) {
            variables.add(triple.getObject());
        }

        return variables;
    }

    private Set<RDFTriple> findAllMatchingPatterns(Set<String> variables) {
        return infoManager.findAllMatchingPatterns(variables);
    }
}
