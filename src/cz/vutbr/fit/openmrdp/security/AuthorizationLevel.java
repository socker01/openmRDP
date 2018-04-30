package cz.vutbr.fit.openmrdp.security;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public enum AuthorizationLevel {
    NONE("None"),
    REQUIRED("Required");

    private final String code;

    AuthorizationLevel(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
