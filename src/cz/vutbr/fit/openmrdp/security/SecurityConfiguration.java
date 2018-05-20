package cz.vutbr.fit.openmrdp.security;

import com.sun.istack.internal.Nullable;

/**
 * This class contains all preferences from the security area. Instance of this class is used in the
 * general {@link cz.vutbr.fit.openmrdp.server.ServerConfiguration}.
 *
 * @author Jiri Koudelka
 * @since 29.04.2018
 */
public final class SecurityConfiguration {
    private final boolean supportSecureConnection;
    @Nullable
    private final UserAuthorizator userAuthorizator;
    @Nullable
    private final String keyStorePassword;
    @Nullable
    private final String keyStorePath;

    private SecurityConfiguration(Builder builder) {
        this.supportSecureConnection = builder.supportSecureConnection;
        this.userAuthorizator = builder.userAuthorizator;
        this.keyStorePassword = builder.keyStorePassword;
        this.keyStorePath = builder.keyStorePath;
    }

    public boolean isSecureConnectionSupported() {
        return supportSecureConnection;
    }

    @Nullable
    public UserAuthorizator getUserAuthorizator() {
        return userAuthorizator;
    }

    @Nullable
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    @Nullable
    public String getKeyStorePath() {
        return keyStorePath;
    }

    static final class Builder {
        private boolean supportSecureConnection;
        @Nullable
        private UserAuthorizator userAuthorizator;
        private String keyStorePassword;
        private String keyStorePath;

        Builder withSupportSecureConnection(boolean supportSecureConnection) {
            this.supportSecureConnection = supportSecureConnection;
            return this;
        }

        Builder withUserAuthorizator(UserAuthorizator userAuthorizator) {
            this.userAuthorizator = userAuthorizator;
            return this;
        }

        Builder withKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
            return this;
        }

        Builder withKeyStorePath(String keyStorePath) {
            this.keyStorePath = keyStorePath;
            return this;
        }

        SecurityConfiguration build() {
            return new SecurityConfiguration(this);
        }
    }
}
