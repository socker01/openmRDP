package cz.vutbr.fit.openmrdp.security;

/**
 * @author Jiri Koudelka
 * @since 29.04.2018
 */
public final class SecurityConfiguration {
   private final boolean supportSecureConnection;
   private final UserAuthenticator userAuthenticator;

    public SecurityConfiguration(boolean supportSecureConnection, UserAuthenticator userAuthenticator) {
        this.supportSecureConnection = supportSecureConnection;
        this.userAuthenticator = userAuthenticator;
    }

    public boolean authorizeUser(String login, String password){
        return userAuthenticator.authorizeUser(login, password);
    }

    public boolean isSupportSecureConnection() {
        return supportSecureConnection;
    }
}
