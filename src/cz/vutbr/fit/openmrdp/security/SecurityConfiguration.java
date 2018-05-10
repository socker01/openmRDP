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

    SecurityConfiguration(boolean supportSecureConnection, @Nullable UserAuthorizator userAuthorizator) {
        this.supportSecureConnection = supportSecureConnection;
        this.userAuthorizator = userAuthorizator;
    }

    public boolean authorizeUser(String login, String password){
        return userAuthorizator.authorizeUser(login, password);
    }

    public boolean isSecureConnectionSupported() {
        return supportSecureConnection;
    }
}
