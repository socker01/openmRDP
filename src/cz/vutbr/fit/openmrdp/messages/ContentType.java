package cz.vutbr.fit.openmrdp.messages;

import com.google.common.annotations.VisibleForTesting;
import com.sun.istack.internal.NotNull;

/**
 * The enumeration that represents the type of the content.
 *
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
public enum ContentType {
    PLANT_QUERY("application/com.awareit.plant"),
    SPARQL_QUERY("application/sparql-query"),
    REDEL("application/com.awareit.redel+xml");

    private final String code;

    ContentType(String code) {
        this.code = code;
    }

    @NotNull
    @VisibleForTesting
    public String getCode() {
        return code;
    }
}
