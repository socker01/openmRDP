package cz.vutbr.fit.openmrdp.query;

import com.google.common.annotations.VisibleForTesting;
import cz.vutbr.fit.openmrdp.exceptions.QueryProcessingException;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.QueryVariable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.base.VariableResourcePair;

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

    public List<String> resolveQuery(MessageBody messageBody, String resourceToSearchName) {

        Query query;
        try {
            query = QueryProcessor.processQuery(messageBody);
        } catch (QuerySyntaxException e) {
            return Collections.emptyList();
        }

        Set<QueryVariable> variables = QueryVariableProcessor.identifyVariables(query.getQueryTriples());
        Map<RDFTriple, Set<RDFTriple>> matchingPatterns = findAllMatchingPatterns(query.getQueryTriples());

        if (setPossibleValuesToVariables(variables, matchingPatterns)) {
            return Collections.emptyList();
        }

        List<List<VariableResourcePair>> possibleVariableCombinations = QueryVariableProcessor.prepareVariableCombinations(variables);
        List<String> correctResourcesNames = new ArrayList<>();

        QueryVariableSetter queryVariableSetter = new QueryVariableSetter(query);
        for (List<VariableResourcePair> variableCombination : possibleVariableCombinations) {
            Set<RDFTriple> substitutedTriples = queryVariableSetter.substituteVariableValues(variableCombination);

            if (infoManager.verifyFacts(substitutedTriples)) {
                correctResourcesNames.add(QueryVariableProcessor.getVariablePairByVariableName(resourceToSearchName, variableCombination).getResourceName());
            }
        }

        return correctResourcesNames;
    }

    private boolean setPossibleValuesToVariables(Set<QueryVariable> variables, Map<RDFTriple, Set<RDFTriple>> matchingPatterns) {
        for (RDFTriple findingTriple : matchingPatterns.keySet()) {
            if (findingTriple.hasTwoVariables()) {
                findAndAddResourcesToSubjectVariable(variables, matchingPatterns, findingTriple);
                findAndAddResourcesToObjectVariable(variables, matchingPatterns, findingTriple);
            } else if (findingTriple.isSubjectVariable()) {
                findAndAddResourcesToSubjectVariable(variables, matchingPatterns, findingTriple);
            } else if (findingTriple.isObjectVariable()) {
                findAndAddResourcesToObjectVariable(variables, matchingPatterns, findingTriple);
            } else {
                if (!verifyFact(findingTriple, matchingPatterns.get(findingTriple))) {
                    return true;
                }
            }
        }

        return false;
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
        for (String validResource : validResources) {
            if (!subjectVariable.getResourceName().contains(validResource)) {
                subjectVariable.getResourceName().add(validResource);
            }
        }
    }

    private void addAppropriateResourceToObjectVariable(QueryVariable objectVariable, Set<String> validResources) {
        for (String validResource : validResources) {
            if (!objectVariable.getResourceName().contains(validResource)) {
                objectVariable.getResourceName().add(validResource);
            }
        }
    }

    @VisibleForTesting
    boolean verifyFact(RDFTriple findingTriple, Set<RDFTriple> rdfTriples) {
        return rdfTriples.size() == 1 && rdfTriples.contains(findingTriple);
    }
}
