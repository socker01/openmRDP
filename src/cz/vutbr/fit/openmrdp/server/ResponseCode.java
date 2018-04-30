package cz.vutbr.fit.openmrdp.server;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public enum ResponseCode {
    OK(200);

    private final int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
