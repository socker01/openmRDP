package cz.vutbr.fit.openmrdp.security;

import com.sun.istack.internal.Nullable;

/**
 * @author Jiri Koudelka
 * @since 29.04.2018
 */
public final class SecurityConfiguration {
   private final boolean supportSecureConnection;
   @Nullable
   private final UserAuthorizator userAuthorizator;

   private final boolean debugMode;

    SecurityConfiguration(boolean supportSecureConnection, @Nullable UserAuthorizator userAuthorizator, boolean debugMode) {
        this.supportSecureConnection = supportSecureConnection;
        this.userAuthorizator = userAuthorizator;
        this.debugMode = debugMode;
    }

    public boolean isSecureConnectionSupported() {
        return supportSecureConnection;
    }

    public UserAuthorizator getUserAuthorizator() {
        return userAuthorizator;
    }

    public boolean isDebugMode() {
        return debugMode;
    }
}
