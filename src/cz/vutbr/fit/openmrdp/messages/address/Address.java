package cz.vutbr.fit.openmrdp.messages.address;

import com.sun.istack.internal.NotNull;

/**
 * Object that represents URL Address without protocol
 *
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
public final class Address {

    @NotNull
    private final String hostAddress;
    @NotNull
    private final String endPoint;

    Address(@NotNull String hostAddress, @NotNull String endpoint) {
        this.hostAddress = hostAddress;
        this.endPoint = endpoint;
    }

    @NotNull
    public String getHostAddress() {
        return hostAddress;
    }

    @NotNull
    public String getEndPoint() {
        return endPoint;
    }
}
