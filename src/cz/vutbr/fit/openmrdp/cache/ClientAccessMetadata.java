package cz.vutbr.fit.openmrdp.cache;

import com.sun.istack.internal.NotNull;

import java.time.Instant;
import java.util.Objects;

/**
 * This objects is used as a key into the map of already logged users.
 *
 * @author Jiri Koudelka
 * @since 07.05.2018
 */
public final class ClientAccessMetadata {

    @NotNull
    private final String login;
    @NotNull
    private final String passwordHash;
    @NotNull
    private final Instant lastAccess;

    public ClientAccessMetadata(@NotNull String login, @NotNull String passwordHash, @NotNull Instant lastAccess) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.lastAccess = lastAccess;
    }

    @NotNull
    public Instant getLastAccess() {
        return lastAccess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAccessMetadata that = (ClientAccessMetadata) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(passwordHash, that.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, passwordHash);
    }
}
