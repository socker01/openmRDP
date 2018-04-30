package cz.vutbr.fit.openmrdp.server;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public enum MessageType {
    GET("GET"),
    POST("POST");

    private final String code;

    MessageType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
