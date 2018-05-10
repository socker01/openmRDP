package cz.vutbr.fit.openmrdp.cache;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;

import java.util.Objects;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public final class ClientEntry {
    @NotNull
    private final String address;
    private final int sequenceNumber;

    //TODO doplnit a o cas a promazavat mapu + dopsat do textu DP

    public ClientEntry(@NotNull String address, int sequenceNumber) {
        this.address = Preconditions.checkNotNull(address);
        this.sequenceNumber = sequenceNumber;
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
