package cz.vutbr.fit.openmrdp.security;

import com.sun.istack.internal.NotNull;

/**
 * @author Jiri Koudelka
 * @since 25.04.2018
 */
public interface UserAuthorizator {
    boolean authorizeUser(@NotNull String login, @NotNull String password);
}
