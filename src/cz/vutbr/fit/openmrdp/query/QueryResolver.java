package cz.vutbr.fit.openmrdp.query;

import com.google.common.annotations.VisibleForTesting;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.exceptions.QueryProcessingException;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.QueryVariable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;

import java.util.*;

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

        Set<QueryVariable> variables = identifyVariables(query.getQueryTriples());
        Map<RDFTriple, Set<RDFTriple>> matchingPatterns = findAllMatchingPatterns(query.getQueryTriples());

        //nastaveni moznych hodnot promennych
        for (RDFTriple findingTriple : matchingPatterns.keySet()) {
            if (findingTriple.hasTwoVariables()) {
                findAndAddResourcesToSubjectVariable(variables, matchingPatterns, findingTriple);
                findAndAddResourcesToObjectVariable(variables, matchingPatterns, findingTriple);
            } else if (findingTriple.isSubjectVariable()){
                findAndAddResourcesToSubjectVariable(variables, matchingPatterns, findingTriple);
            } else if (findingTriple.isObjectVariable()){
                findAndAddResourcesToObjectVariable(variables, matchingPatterns, findingTriple);
            } else {
                if(!verifyFact(findingTriple, matchingPatterns.get(findingTriple))){
                    return null;
                }
            }
        }


        return "";
    }

    @VisibleForTesting
    Set<QueryVariable> identifyVariables(Set<RDFTriple> triples) {
        Set<QueryVariable> variables = new HashSet<>();
        for (RDFTriple triple : triples) {
            variables.addAll(findVariable(triple));
        }

        return variables;
    }

    private Set<QueryVariable> findVariable(RDFTriple triple) {
        Set<QueryVariable> variables = new HashSet<>();

        if (triple.getSubject().startsWith("?")) {
            variables.add(new QueryVariable(triple.getSubject()));
        }

        if (triple.getObject().startsWith("?")) {
            variables.add(new QueryVariable(triple.getObject()));
        }

        return variables;
    }

    private Map<RDFTriple, Set<RDFTriple>> findAllMatchingPatterns(Set<RDFTriple> locatingTriples) {

        Map<RDFTriple, Set<RDFTriple>> allMatchingPatterns = new HashMap<>();

        for (RDFTriple triple : locatingTriples) {
            Set<RDFTriple> matchingTriples = infoManager.findMatchingPatterns(triple);
            allMatchingPatterns.put(triple, matchingTriples);
        }

        return allMatchingPatterns;
    }

    private void findAndAddResourcesToSubjectVariable(Set<QueryVariable> variables, Map<RDFTriple, Set<RDFTriple>> matchingPatterns, RDFTriple findingTriple) {
        Set<String> resources = findValidResourcesForSubjectVariable(matchingPatterns.get(findingTriple));
        QueryVariable subjectVariable = findVariableByName(findingTriple.getSubject(), variables);
        addAppropriateResourceToSubjectVariable(subjectVariable, resources);
    }

    private void findAndAddResourcesToObjectVariable(Set<QueryVariable> variables, Map<RDFTriple, Set<RDFTriple>> matchingPatterns, RDFTriple findingTriple) {
        Set<String> resources = findValidResourcesForObjectVariable(matchingPatterns.get(findingTriple));
        QueryVariable objectVariable = findVariableByName(findingTriple.getObject(), variables);
        addAppropriateResourceToObjectVariable(objectVariable, resources);
    }

    @VisibleForTesting
    Set<String> findValidResourcesForSubjectVariable(Set<RDFTriple> rdfTriples) {
        Set<String> resourceNames = new HashSet<>();
        for (RDFTriple triple : rdfTriples) {
            resourceNames.add(triple.getSubject());
        }

        return resourceNames;
    }

    @VisibleForTesting
    Set<String> findValidResourcesForObjectVariable(Set<RDFTriple> rdfTriples) {
        Set<String> resourceNames = new HashSet<>();
        for (RDFTriple triple : rdfTriples) {
            resourceNames.add(triple.getObject());
        }

        return resourceNames;
    }

    private QueryVariable findVariableByName(String objectVariableName, Set<QueryVariable> variables) {
        for (QueryVariable variable : variables) {
            if (variable.getVariableName().equals(objectVariableName)) {
                return variable;
            }
        }

        throw new QueryProcessingException("Exception during query processing");
    }

    private void addAppropriateResourceToSubjectVariable(QueryVariable subjectVariable, Set<String> validResources) {
        subjectVariable.getResourceName().addAll(validResources);
    }

    private void addAppropriateResourceToObjectVariable(QueryVariable objectVariable, Set<String> validResources) {
        objectVariable.getResourceName().addAll(validResources);
    }

    @VisibleForTesting
    boolean verifyFact(RDFTriple findingTriple, Set<RDFTriple> rdfTriples) {
        return rdfTriples.size() == 1 && rdfTriples.contains(findingTriple);
    }

    private List<Integer> findCountsOfPossibleVariableResources(Set<QueryVariable> variables) {
        Set<Integer> sizes = new HashSet<>();

        for (QueryVariable variable : variables) {
            sizes.add(variable.getResourceName().size());
        }

        List<Integer> listOfSizes = new ArrayList<>(sizes);
        Collections.sort(listOfSizes);

        return listOfSizes;
    }
}
