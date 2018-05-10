package cz.vutbr.fit.openmrdp.server;

/**
 * @author Jiri Koudelka
 * @since 29.04.2018
 */
public final class ServerConfiguration {
    private final String ipAddress;
    private final int port;
    //TODO: add security configuration

    public ServerConfiguration(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
