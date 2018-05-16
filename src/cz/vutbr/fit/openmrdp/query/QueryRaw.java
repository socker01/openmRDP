package cz.vutbr.fit.openmrdp.query;

/**
 * @author Jiri Koudelka
 * @since 14.05.2018
 */
public final class QueryRaw {
    private final String resourceName;
    private final String conditions;

    public QueryRaw(String resourceName, String conditions) {
        this.resourceName = resourceName;
        this.conditions = conditions;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getConditions() {
        return conditions;
    }
}
