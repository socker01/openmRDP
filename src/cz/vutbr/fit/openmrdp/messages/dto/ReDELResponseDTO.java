package cz.vutbr.fit.openmrdp.messages.dto;

import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import cz.vutbr.fit.openmrdp.messages.address.Address;

/**
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
public final class ReDELResponseDTO {

    @NotNull
    private final Address address;
    @NotNull
    private final int sequenceNumber;
    private final String resourceLocation;

    private ReDELResponseDTO(Builder builder) {
        this.address = Preconditions.checkNotNull(builder.address);
        this.sequenceNumber = Preconditions.checkNotNull(builder.sequenceNumber);
        this.resourceLocation = builder.resourceLocation;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public Address getAddress() {
        return address;
    }

    public static class Builder{
        private Address address;
        private int sequenceNumber;
        private String resourceLocation;

        public Builder withAddress(Address address){
            this.address = address;
            return this;
        }

        public Builder withSequenceNumber(int sequenceNumber){
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public Builder withResourceLocation(String resourceLocation){
            this.resourceLocation = resourceLocation;
            return this;
        }

        public ReDELResponseDTO build(){
            return new ReDELResponseDTO(this);
        }
    }
}
