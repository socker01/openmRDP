package cz.vutbr.fit.openmrdp.model.base;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;

import java.util.Objects;

/**
 * Internal object used in resolution of IDENTIFY queries.
 *
 * @author Jiri Koudelka
 * @since 01.04.2018.
 */
public final class VariableResourcePair implements Comparable {
    @NotNull
    private final String variableName;
    @NotNull
    private final String resourceName;

    public VariableResourcePair(@NotNull String variableName, @NotNull String resourceName) {
        this.variableName = Preconditions.checkNotNull(variableName);
        this.resourceName = Preconditions.checkNotNull(resourceName);
    }

    @NotNull
    public String getVariableName() {
        return variableName;
    }

    @NotNull
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

    @Override
    public int compareTo(Object o) {
        VariableResourcePair v = (VariableResourcePair) o;
        return this.getVariableName().compareTo(v.getVariableName());
    }
}
