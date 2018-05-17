package cz.vutbr.fit.openmrdp.cache;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;

import java.time.Instant;
import java.util.Objects;

/**
 * This object is used as a key into the map of prepared messages.
 *
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public final class ClientEntry {
    @NotNull
    private final String address;
    private final int sequenceNumber;
    @NotNull
    private Instant created;

    public ClientEntry(@NotNull String address, int sequenceNumber, @NotNull Instant created) {
        this.address = Preconditions.checkNotNull(address);
        this.sequenceNumber = sequenceNumber;
        this.created = created;
    }

    @NotNull
    public Instant getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientEntry that = (ClientEntry) o;
        return sequenceNumber == that.sequenceNumber &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, sequenceNumber);
    }
}
