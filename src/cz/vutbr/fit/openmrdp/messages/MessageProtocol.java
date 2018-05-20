package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;

/**
 * Enumeration of the protocols used by OpenmRDP
 *
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
public enum MessageProtocol {
    HTTP("HTTP", "1.0"),
    HTTPS("HTTPS", "1.0"),
    MRDP("mRDP", "1.0");

    private final String name;
    private final String version;

    MessageProtocol(@NotNull String name, @NotNull String version) {
        this.name = name;
        this.version = version;
    }

    @NotNull
    String getProtocolCode(){
        return name + "/" + version;
    }

    @NotNull
    static MessageProtocol fromString(@NotNull String protocolRaw){
        return Arrays.stream(MessageProtocol.values())
                .filter(messageProtocol -> messageProtocol.getProtocolCode().equals(protocolRaw))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("There is no value '%s' in Enum %s", protocolRaw, getEnumName())));
    }

    @NotNull
    private static String getEnumName(){
        return "MessageProtocol";
    }

    @NotNull
    public String getName() {
        return name;
    }
}
