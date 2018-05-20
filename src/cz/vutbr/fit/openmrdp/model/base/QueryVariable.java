package cz.vutbr.fit.openmrdp.model.base;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This object contains variables and the list of their potential values.
 *
 * @author Jiri Koudelka
 * @since 31.03.2018.
 */
public final class QueryVariable {

    @NotNull
    private final String variableName;
    @NotNull
    private List<String> resourceName = new ArrayList<>();

    public QueryVariable(@NotNull String variableName) {
        this.variableName = Preconditions.checkNotNull(variableName);
    }

    @NotNull
    public String getVariableName() {
        return variableName;
    }

    @NotNull
    public List<String> getResourceName() {
        return resourceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryVariable that = (QueryVariable) o;
        return Objects.equals(variableName, that.variableName) &&
                Objects.equals(resourceName, that.resourceName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(variableName, resourceName);
    }
}
