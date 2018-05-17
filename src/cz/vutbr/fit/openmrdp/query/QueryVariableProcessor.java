package cz.vutbr.fit.openmrdp.query;

import cz.vutbr.fit.openmrdp.exceptions.QueryProcessingException;
import cz.vutbr.fit.openmrdp.model.base.QueryVariable;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.base.VariableResourcePair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Processor which works with variables.
 *
 * This processor creates possible combinations of the variables and identifies the variable in the {@link RDFTriple}.
 *
 * @author Jiri Koudelka
 * @since 04.04.2018.
 */
final class QueryVariableProcessor {

    /**
     * Create matrix of the all possible variable combinations
     *
     * @param variables - {@link Set} of all variables
     * @return - matrix of all possible variable combinations
     */
    static List<List<VariableResourcePair>> prepareVariableCombinations(Set<QueryVariable> variables) {

        int numberOfCombinations = 1;
        List<List<VariableResourcePair>> possibleCombinations = new ArrayList<>();

        for (QueryVariable variable : variables) {
            numberOfCombinations *= variable.getResourceName().size();
        }

        for (int i = 1; i <= numberOfCombinations; i++) {
            List<VariableResourcePair> possibleCombination = new ArrayList<>();
            for (QueryVariable variable : variables) {
                VariableResourcePair pair = new VariableResourcePair(variable.getVariableName(), variable.getResourceName().get(i % variable.getResourceName().size()));
                possibleCombination.add(pair);
            }

            possibleCombinations.add(possibleCombination);
        }

        return possibleCombinations;
    }

    /**
     * Identify variables in the {@link RDFTriple}
     *
     * @param triples - {@link RDFTriple}
     * @return - {@link Set} of identified variables
     */
    static Set<QueryVariable> identifyVariables(Set<RDFTriple> triples) {
        Set<QueryVariable> variables = new HashSet<>();
        for (RDFTriple triple : triples) {
            variables.addAll(findVariable(triple));
        }

        return variables;
    }

    private static Set<QueryVariable> findVariable(RDFTriple triple) {
        Set<QueryVariable> variables = new HashSet<>();

        if (triple.getSubject().startsWith("?")) {
            variables.add(new QueryVariable(triple.getSubject()));
        }

        if (triple.getObject().startsWith("?")) {
            variables.add(new QueryVariable(triple.getObject()));
        }

        return variables;
    }

    static VariableResourcePair getVariablePairByVariableName(String variableName, List<VariableResourcePair> variables) {
        for (VariableResourcePair variablePair : variables) {
            if (variablePair.getVariableName().equals(variableName)) {
                return variablePair;
            }
        }

        throw new QueryProcessingException("Invalid variable name");
    }
}
