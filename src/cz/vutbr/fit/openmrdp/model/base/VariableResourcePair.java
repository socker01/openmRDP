package cz.vutbr.fit.openmrdp.model.base;

/**
 * @author Jiri Koudelka
 * @since 01.04.2018.
 */
public final class VariableResourcePair {
    private final String variableName;
    private final String resourceName;

    public VariableResourcePair(String variableName, String resourceName) {
        this.variableName = variableName;
        this.resourceName = resourceName;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
