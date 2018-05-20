package cz.vutbr.fit.openmrdp.query;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.exceptions.QueryProcessingException;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.base.QueryVariable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.base.VariableResourcePair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link QueryResolver} resolves the identify queries and returns found resources.
 *
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public final class QueryResolver {

    @NotNull
    private final InfoManager infoManager;

    public QueryResolver(@NotNull InfoManager infoManager) {
        this.infoManager = Preconditions.checkNotNull(infoManager);
    }

    /**
     * Resolve the identify query and find list of the resource names
     *
     * @param messageBody          - {@link MessageBody} that contains identify query
     * @param resourceToSearchName - the name of the resource you are looking for
     * @return - {@link List} of found resources
     */
    @NotNull
    public List<String> resolveQuery(@NotNull MessageBody messageBody, @NotNull String resourceToSearchName) {

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

    private boolean setPossibleValuesToVariables(@NotNull Set<QueryVariable> variables, @NotNull Map<RDFTriple, Set<RDFTriple>> matchingPatterns) {
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

    @NotNull
    private Map<RDFTriple, Set<RDFTriple>> findAllMatchingPatterns(@NotNull Set<RDFTriple> locatingTriples) {
        return locatingTriples.stream().collect(Collectors.toMap(v -> v, infoManager::findMatchingPatterns));
    }

    private void findAndAddResourcesToSubjectVariable(@NotNull Set<QueryVariable> variables, @NotNull Map<RDFTriple, Set<RDFTriple>> matchingPatterns,
                                                      @NotNull RDFTriple findingTriple) {
        Set<String> resources = findValidResourcesForSubjectVariable(matchingPatterns.get(findingTriple));
        QueryVariable subjectVariable = findVariableByName(findingTriple.getSubject(), variables);
        addAppropriateResourceToSubjectVariable(subjectVariable, resources);
    }

    private void findAndAddResourcesToObjectVariable(@NotNull Set<QueryVariable> variables, @NotNull Map<RDFTriple, Set<RDFTriple>> matchingPatterns,
                                                     @NotNull RDFTriple findingTriple) {
        Set<String> resources = findValidResourcesForObjectVariable(matchingPatterns.get(findingTriple));
        QueryVariable objectVariable = findVariableByName(findingTriple.getObject(), variables);
        addAppropriateResourceToObjectVariable(objectVariable, resources);
    }

    @NotNull
    @VisibleForTesting
    Set<String> findValidResourcesForSubjectVariable(@NotNull Set<RDFTriple> rdfTriples) {
        Set<String> resourceNames = new HashSet<>();
        for (RDFTriple triple : rdfTriples) {
            resourceNames.add(triple.getSubject());
        }

        return resourceNames;
    }

    @NotNull
    @VisibleForTesting
    Set<String> findValidResourcesForObjectVariable(@NotNull Set<RDFTriple> rdfTriples) {
        Set<String> resourceNames = new HashSet<>();
        for (RDFTriple triple : rdfTriples) {
            resourceNames.add(triple.getObject());
        }

        return resourceNames;
    }

    @NotNull
    private QueryVariable findVariableByName(@NotNull String objectVariableName, @NotNull Set<QueryVariable> variables) {
        return variables
                .stream()
                .filter(queryVariable -> queryVariable.getVariableName().equals(objectVariableName))
                .findFirst()
                .orElseThrow(() -> new QueryProcessingException("Exception during query processing"));
    }

    private void addAppropriateResourceToSubjectVariable(@NotNull QueryVariable subjectVariable, @NotNull Set<String> validResources) {
        for (String validResource : validResources) {
            if (!subjectVariable.getResourceName().contains(validResource)) {
                subjectVariable.getResourceName().add(validResource);
            }
        }
    }

    private void addAppropriateResourceToObjectVariable(@NotNull QueryVariable objectVariable, @NotNull Set<String> validResources) {
        for (String validResource : validResources) {
            if (!objectVariable.getResourceName().contains(validResource)) {
                objectVariable.getResourceName().add(validResource);
            }
        }
    }

    @NotNull
    @VisibleForTesting
    boolean verifyFact(@NotNull RDFTriple findingTriple, @NotNull Set<RDFTriple> rdfTriples) {
        return rdfTriples.size() == 1 && rdfTriples.contains(findingTriple);
    }
}
