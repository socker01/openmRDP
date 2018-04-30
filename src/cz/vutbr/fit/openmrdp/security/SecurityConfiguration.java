package cz.vutbr.fit.openmrdp.security;

/**
 * @author Jiri Koudelka
 * @since 29.04.2018
 */
public final class SecurityConfiguration {
   private final boolean supportSecureConnection;
   private final UserAuthorizator userAuthorizator;

    public SecurityConfiguration(boolean supportSecureConnection, UserAuthorizator userAuthorizator) {
        this.supportSecureConnection = supportSecureConnection;
        this.userAuthorizator = userAuthorizator;
    }

    public boolean authorizeUser(String login, String password){
        return userAuthorizator.authorizeUser(login, password);
    }

    public boolean isSupportSecureConnection() {
        return supportSecureConnection;
    }
}
