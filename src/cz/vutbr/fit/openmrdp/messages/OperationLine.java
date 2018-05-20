package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.NotNull;

import java.util.Objects;

/**
 * Object of {@link OperationLine} type represents first line in the LOCATE or IDENTIFY messages.
 *
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
final class OperationLine {

    @NotNull
    private final OperationType operationType;
    @NotNull
    private final String resourceName;
    @NotNull
    private final MessageProtocol protocol;

    OperationLine(@NotNull OperationType operationType, @NotNull String resourceName, @NotNull MessageProtocol protocol) {
        this.operationType = operationType;
        this.resourceName = resourceName;
        this.protocol = protocol;
    }

    @NotNull
    String createOperationLineString(){
        return operationType.getCode() + " " + resourceName + " " + protocol.getProtocolCode();
    }

    @NotNull
    OperationType getOperationType() {
        return operationType;
    }

    @NotNull
    String getResourceName() {
        return resourceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationLine that = (OperationLine) o;
        return operationType == that.operationType &&
                Objects.equals(resourceName, that.resourceName) &&
                protocol == that.protocol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationType, resourceName, protocol);
    }
}
