package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;

/**
 * Enumeration of operation types used by OpenmRDP
 *
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public enum OperationType {
    IDENTIFY("IDENTIFY", 4),
    LOCATE("LOCATE", 2),
    POST("POST", 4);

    private final String code;
    private final int headersCount;

    OperationType(@NotNull String code, @NotNull int headersCount) {
        this.code = code;
        this.headersCount = headersCount;
    }

    @NotNull
    static OperationType fromString(@NotNull String code){
        return Arrays.stream(OperationType.values())
                .filter(operationType -> operationType.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("There is no value with name '%s' in Enum %s", code, getEnumName())));
    }

    @NotNull
    private static String getEnumName(){
        return "OperationType";
    }

    @NotNull
    public String getCode() {
        return code;
    }

    int getHeadersCount() {
        return headersCount;
    }
}
