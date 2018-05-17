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
    public static SecurityConfiguration createSecureSecurityConfiguration(@NotNull UserAuthorizator userAuthorizator) {
        return new SecurityConfiguration(true, userAuthorizator, false);
    }

    /**
     * Create instance of {@link SecurityConfiguration} for secure communication with debug flag
     *
     * @param userAuthorizator - {@link UserAuthorizator} for user authorization
     * @return - {@link SecurityConfiguration}
     */
    public static SecurityConfiguration createDebugSecureSecurityConfiguration(@NotNull UserAuthorizator userAuthorizator) {
        return new SecurityConfiguration(true, userAuthorizator, true);
    }

    /**
     * Create instance of {@link SecurityConfiguration} for non-secure communication
     * @return - {@link SecurityConfiguration}
     */
    public static SecurityConfiguration createNonSecureSecurityConfiguration() {
        return new SecurityConfiguration(false, null, false);
    }
}
