package cz.vutbr.fit.openmrdp.query;

import com.google.common.annotations.VisibleForTesting;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.RDFTriple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        Map<RDFTriple, Set<RDFTriple>> matchingPatterns = findAllMatchingPatterns(query.getQueryTriples());
        //TODO: continue here

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

    //TODO: add test
    private Map<RDFTriple, Set<RDFTriple>> findAllMatchingPatterns(Set<RDFTriple> locatingTriples) {

        Map<RDFTriple, Set<RDFTriple>> allMatchingPatterns = new HashMap<>();

        for(RDFTriple triple : locatingTriples){
            Set<RDFTriple> matchingTriples = infoManager.findMatchingPatterns(triple);
            allMatchingPatterns.put(triple, matchingTriples);
        }

        return allMatchingPatterns;
    }
}
