package cz.vutbr.fit.openmrdp.query;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
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

    @NotNull
    private final Query query;

    QueryVariableSetter(@NotNull Query query) {
        this.query = Preconditions.checkNotNull(query);
    }

    /**
     * Create {@link Set} of the potential values of variables
     *
     * @param variables - {@link List} of variables
     * @return - {@link Set} of {@link RDFTriple} objects with the potential values
     */
    @NotNull
    Set<RDFTriple> substituteVariableValues(@NotNull List<VariableResourcePair> variables) {
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

    @NotNull
    private RDFTriple substituteVariablesWithValues(@NotNull RDFTriple findingTriple, @NotNull List<VariableResourcePair> variables) {
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

    @NotNull
    private String getSubstitutedObject(@NotNull RDFTriple findingTriple, @NotNull List<VariableResourcePair> variables) {
        String substitutedObject = null;
        if (findingTriple.isObjectVariable()) {
            substitutedObject = findVariableByName(findingTriple.getObject(), variables);
        }

        return substitutedObject;
    }

    @NotNull
    private String getSubstitutedSubject(@NotNull RDFTriple findingTriple, @NotNull List<VariableResourcePair> variables) {
        String substitutedSubject = null;
        if (findingTriple.isSubjectVariable()) {
            substitutedSubject = findVariableByName(findingTriple.getSubject(), variables);
        }

        return substitutedSubject;
    }

    @NotNull
    private String findVariableByName(@NotNull String variableName, @NotNull List<VariableResourcePair> variables) {
        return variables.stream()
                .filter(v -> v.getVariableName().equals(variableName))
                .findFirst()
                .orElseThrow(() -> new QueryProcessingException("Invalid variable name in variable substitution process"))
                .getResourceName();
    }
}
