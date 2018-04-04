package cz.vutbr.fit.openmrdp.model.base;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jiri Koudelka
 * @since 31.03.2018.
 */
public final class QueryVariable {

    private final String variableName;
    private boolean found = false;
    private List<String> resourceName = new ArrayList<>();

    public QueryVariable(@NotNull String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public List<String> getResourceName() {
        return resourceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryVariable that = (QueryVariable) o;
        return found == that.found &&
                Objects.equals(variableName, that.variableName) &&
                Objects.equals(resourceName, that.resourceName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(variableName, found, resourceName);
    }
}
