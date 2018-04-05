package cz.vutbr.fit.openmrdp.model.base;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableResourcePair that = (VariableResourcePair) o;
        return Objects.equals(variableName, that.variableName) &&
                Objects.equals(resourceName, that.resourceName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(variableName, resourceName);
    }
}
