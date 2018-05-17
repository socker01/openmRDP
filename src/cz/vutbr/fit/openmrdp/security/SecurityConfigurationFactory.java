package cz.vutbr.fit.openmrdp.security;

import com.sun.istack.internal.NotNull;

/**
 * Factory for creating of SecurityConfiguration.
 *
 * @author Jiri Koudelka
 * @since 07.05.2018
 */
public final class SecurityConfigurationFactory {

    public static SecurityConfiguration createSecureSecurityConfiguration(@NotNull UserAuthorizator userAuthorizator) {
        return new SecurityConfiguration(true, userAuthorizator, false);
    }

    public static SecurityConfiguration createDebugSecureSecurityConfiguration(@NotNull UserAuthorizator userAuthorizator) {
        return new SecurityConfiguration(true, userAuthorizator, true);
    }

    public static SecurityConfiguration createNonSecureSecurityConfiguration() {
        return new SecurityConfiguration(false, null, false);
    }
}
