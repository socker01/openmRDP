package cz.vutbr.fit.openmrdp.messages.dto;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.messages.address.Address;
import cz.vutbr.fit.openmrdp.model.base.Resource;

import java.util.List;

/**
 * Data Transfer Object which is used for storing of ReDEL message related data
 *
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
public final class ReDELResponseDTO {

    @NotNull
    private final Address address;
    @Nullable
    private final Integer sequenceNumber;
    @NotNull
    private final List<Resource> resources;

    private ReDELResponseDTO(Builder builder) {
        this.address = Preconditions.checkNotNull(builder.address);
        this.sequenceNumber = builder.sequenceNumber;
        this.resources = Preconditions.checkNotNull(builder.resources);
    }

    @Nullable
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @NotNull
    public Address getAddress() {
        return address;
    }

    @NotNull
    public List<Resource> getResources() {
        return resources;
    }

    public static class Builder {
        private Address address;
        private Integer sequenceNumber;
        private List<Resource> resources;

        public Builder withAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder withSequenceNumber(Integer sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public Builder withResource(List<Resource> resource) {
            this.resources = resource;
            return this;
        }

        public ReDELResponseDTO build() {
            return new ReDELResponseDTO(this);
        }
    }
}
