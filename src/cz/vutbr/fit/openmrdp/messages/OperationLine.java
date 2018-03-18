package cz.vutbr.fit.openmrdp.messages;

import java.util.Objects;

/**
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
final class OperationLine {

    private final OperationType operationType;
    private final String resourceName;
    private final MessageProtocol protocol;

    OperationLine(OperationType operationType, String resourceName, MessageProtocol protocol) {
        this.operationType = operationType;
        this.resourceName = resourceName;
        this.protocol = protocol;
    }

    String createOperationLineString(){
        return operationType.getCode() + " " + resourceName + " " + protocol.getProtocolCode();
    }

    OperationType getOperationType() {
        return operationType;
    }

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
