package cz.vutbr.fit.openmrdp.messages;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.address.Address;
import cz.vutbr.fit.openmrdp.messages.address.AddressParser;

import java.util.Map;

/**
 * Represents general message which can contains information about LOCATE, IDENTIFY and ReDEL messages
 *
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public final class BaseMessage {

    @NotNull
    private final OperationLine operationLine;
    @NotNull
    private final Map<HeaderType, String> headers;
    @Nullable
    private final MessageBody body;

    BaseMessage(@NotNull OperationLine operationLine, @NotNull Map<HeaderType, String> headers, @Nullable MessageBody body) {
        this.operationLine = Preconditions.checkNotNull(operationLine);
        this.headers = Preconditions.checkNotNull(headers);
        this.body = body;
    }

    @NotNull
    OperationLine getOperationLine() {
        return operationLine;
    }

    @VisibleForTesting
    @NotNull
    public Map<HeaderType, String> getHeaders() {
        return headers;
    }

    @NotNull
    public OperationType getOperationType() {
        return operationLine.getOperationType();
    }

    @NotNull
    public String getResourceName() {
        return operationLine.getResourceName();
    }

    public int getSequenceNumber() {
        return Integer.parseInt(headers.get(HeaderType.NSEQ).trim());
    }

    @NotNull
    public Address getHostAddress() throws AddressSyntaxException {
        return AddressParser.parseAddressHostAndEndpoint(headers.get(HeaderType.CALLBACK_URI));
    }

    @Nullable
    public MessageBody getMessageBody() {
        return body;
    }

    @Nullable
    String getBodyQuery() {
        return body == null ? null : body.getQuery();
    }
}
