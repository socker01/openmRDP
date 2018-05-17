package cz.vutbr.fit.openmrdp.query;

import cz.vutbr.fit.openmrdp.exceptions.QueryProcessingException;
import cz.vutbr.fit.openmrdp.model.base.RDFTriple;
import cz.vutbr.fit.openmrdp.model.base.VariableResourcePair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used for setting of potential values of variables.
 *
 * @author Jiri Koudelka
 * @since 04.04.2018.
 */
final class QueryVariableSetter {

    private final Query query;

    QueryVariableSetter(Query query) {
        this.query = query;
    }

    /**
     * Create {@link Set} of the potential values of variables
     *
     * @param variables - {@link List} of variables
     * @return - {@link Set} of {@link RDFTriple} objects with the potential values
     */
    Set<RDFTriple> substituteVariableValues(List<VariableResourcePair> variables) {
        Set<RDFTriple> triplesWithVariables = new HashSet<>();

        for (RDFTriple findingTriple : query.getQueryTriples()) {
            if (findingTriple.isObjectVariable() || findingTriple.isSubjectVariable()) {
                triplesWithVariables.add(substituteVariablesWithValues(findingTriple, variables));
            } else {
                triplesWithVariables.add(findingTriple);
            }
        }

        return triplesWithVariables;
    }

    private RDFTriple substituteVariablesWithValues(RDFTriple findingTriple, List<VariableResourcePair> variables) {
        String substitutedSubject = getSubstitutedSubject(findingTriple, variables);
        String substitutedObject = getSubstitutedObject(findingTriple, variables);

        if (substitutedSubject != null && substitutedObject != null) {
            return new RDFTriple(substitutedSubject, findingTriple.getPredicate(), substitutedObject);
        } else if (substitutedSubject != null) {
            return new RDFTriple(substitutedSubject, findingTriple.getPredicate(), findingTriple.getObject());
        } else if (substitutedObject != null) {
            return new RDFTriple(findingTriple.getSubject(), findingTriple.getPredicate(), substitutedObject);
        }

        throw new IllegalStateException("There is no variable to substitute");
    }

    private String getSubstitutedObject(RDFTriple findingTriple, List<VariableResourcePair> variables) {
        String substitutedObject = null;
        if (findingTriple.isObjectVariable()) {
            substitutedObject = findVariableByName(findingTriple.getObject(), variables);
        }

        return substitutedObject;
    }

    private String getSubstitutedSubject(RDFTriple findingTriple, List<VariableResourcePair> variables) {
        String substitutedSubject = null;
        if (findingTriple.isSubjectVariable()) {
            substitutedSubject = findVariableByName(findingTriple.getSubject(), variables);
        }

        return substitutedSubject;
    }

    private String findVariableByName(String variableName, List<VariableResourcePair> variables) {
        for (VariableResourcePair pair : variables) {
            if (pair.getVariableName().equals(variableName)) {
                return pair.getResourceName();
            }
        }

        throw new QueryProcessingException("Invalid variable name in variable substitution process");
    }
}
