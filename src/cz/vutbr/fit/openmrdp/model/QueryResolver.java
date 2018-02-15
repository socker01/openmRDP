package cz.vutbr.fit.openmrdp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class QueryResolver {

    //TODO: otestovat nalazeni vsech relevantnich patternu pro danou query
    public void resolveQuery(Query query){
        //TODO: odstranit duplicity
        List<String> variables = identifyVariables(query.getQueryTriples());
        List<RDFTriple> matchingPatterns = findAllMatchingPatterns(variables);
    }

    private List<String> identifyVariables(List<RDFTriple> triples){
        List<String> variables = new ArrayList<>();
        for(RDFTriple triple : triples){
            variables.addAll(findVariable(triple));
        }

        return variables;
    }

    private List<String> findVariable(RDFTriple triple) {
        List<String> variables = new ArrayList<>();

        if(triple.getSubject().startsWith("?")){
            variables.add(triple.getSubject());
        }

        if(triple.getObject().startsWith("?")){
            variables.add(triple.getObject());
        }

        return variables;
    }

    private List<RDFTriple> findAllMatchingPatterns(List<String> variables) {
        return InfoManager.findAllMatchingPatterns(variables);
    }
}
