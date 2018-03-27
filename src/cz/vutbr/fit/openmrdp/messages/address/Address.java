package cz.vutbr.fit.openmrdp.messages.address;

/**
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
public final class Address {

    private final String hostAddress;
    private final String endPoint;

    Address(String hostAddress, String endpoint) {

        this.hostAddress = hostAddress;
        this.endPoint = endpoint;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
