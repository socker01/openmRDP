package cz.vutbr.fit.openmrdp.messages;

/**
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
public enum ContentType {
    PLANT_QUERY("application/com.awareit.plant"),
    SPARQL_QUERY("application/sparql-query");

    private final String code;

    ContentType(String code) {
        this.code = code;
    }

    String getCode() {
        return code;
    }
}
