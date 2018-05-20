package cz.vutbr.fit.openmrdp.security;

import com.sun.istack.internal.NotNull;

/**
 * Factory for creating of SecurityConfiguration.
 *
 * @author Jiri Koudelka
 * @since 07.05.2018
 */
public final class SecurityConfigurationFactory {

    /**
     * Create instance of {@link SecurityConfiguration} for secure communication
     *
     * @param userAuthorizator - {@link UserAuthorizator} for user authorization
     * @return - {@link SecurityConfiguration}
     */
    @NotNull
    public static SecurityConfiguration createSecureSecurityConfiguration(@NotNull UserAuthorizator userAuthorizator,
                                                                          @NotNull String keyStorePath,
                                                                          @NotNull String keyStorePassword) {
        return new SecurityConfiguration.Builder()
                .withSupportSecureConnection(true)
                .withUserAuthorizator(userAuthorizator)
                .withKeyStorePath(keyStorePath)
                .withKeyStorePassword(keyStorePassword)
                .build();
    }

    /**
     * Create instance of {@link SecurityConfiguration} for non-secure communication
     * @return - {@link SecurityConfiguration}
     */
    @NotNull
    public static SecurityConfiguration createNonSecureSecurityConfiguration() {
        return new SecurityConfiguration.Builder()
                .withSupportSecureConnection(false)
                .withUserAuthorizator(null)
                .withKeyStorePath(null)
                .withKeyStorePassword(null)
                .build();
    }
}
