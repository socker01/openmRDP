package cz.vutbr.fit.openmrdp.query;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;

/**
 * Data transfer object used for storing of the information from query in the {@link String} objects.
 *
 * @author Jiri Koudelka
 * @since 14.05.2018
 */
public final class QueryRaw {
    @NotNull
    private final String resourceName;
    @NotNull
    private final String conditions;

    QueryRaw(@NotNull String resourceName, @NotNull String conditions) {
        this.resourceName = Preconditions.checkNotNull(resourceName);
        this.conditions = Preconditions.checkNotNull(conditions);
    }

    @NotNull
    public String getResourceName() {
        return resourceName;
    }

    @NotNull
    public String getConditions() {
        return conditions;
    }
}
