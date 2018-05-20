package cz.vutbr.fit.openmrdp.server;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.security.SecurityConfiguration;

/**
 * Configuration object which contains all preferences used by server.
 *
 * @author Jiri Koudelka
 * @since 29.04.2018
 */
public final class ServerConfiguration {
    @NotNull
    private final String ipAddress;
    private final int port;
    @NotNull
    private final SecurityConfiguration securityConfiguration;

    public ServerConfiguration(@NotNull String ipAddress, int port, @NotNull SecurityConfiguration securityConfiguration) {
        this.ipAddress = Preconditions.checkNotNull(ipAddress);
        this.port = port;
        this.securityConfiguration = Preconditions.checkNotNull(securityConfiguration);
    }

    @NotNull
    public String getIpAddress() {
        return ipAddress;
    }

    @NotNull
    public int getPort() {
        return port;
    }

    @NotNull
    public SecurityConfiguration getSecurityConfiguration() {
        return securityConfiguration;
    }
}
