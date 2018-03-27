package cz.vutbr.fit.openmrdp.model;

import com.google.common.annotations.VisibleForTesting;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class QueryResolver {

    private final InfoManager infoManager;

    public QueryResolver() {
        this.infoManager = new InfoManager(new InformationBaseProdService());
    }

    public void resolveQuery(Query query) {
        Set<String> variables = identifyVariables(query.getQueryTriples());
        Set<RDFTriple> matchingPatterns = findAllMatchingPatterns(variables);
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

    @VisibleForTesting
    Set<RDFTriple> findAllMatchingPatterns(Set<String> variables) {
        return infoManager.findAllMatchingPatterns(variables);
    }
}
