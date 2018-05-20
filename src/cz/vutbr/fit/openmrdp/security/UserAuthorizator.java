package cz.vutbr.fit.openmrdp.security;

import com.sun.istack.internal.NotNull;

/**
 * Interface user authorization. If you want to authorize user which communicates with server,
 * you have to implement this interface and inject it into the {@link SecurityConfiguration}.
 *
 * @author Jiri Koudelka
 * @since 25.04.2018
 */
public interface UserAuthorizator {

    /**
     * Authorize user
     *
     * @param login    - user login
     * @param password - user password
     * @return - true if the credentials are correct, false if not
     */
    boolean authorizeUser(@NotNull String login, @NotNull String password);
}
