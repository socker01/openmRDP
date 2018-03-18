package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.address.Address;
import cz.vutbr.fit.openmrdp.messages.address.AddressParser;

import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public final class BaseMessage {

    private final OperationLine operationLine;
    private final Map<HeaderType, String> headers;
    private final String body;

    BaseMessage(OperationLine operationLine, Map<HeaderType, String> headers, @Nullable String body){
        this.operationLine = operationLine;
        this.headers = headers;
        this.body = body;
    }

    OperationLine getOperationLine() {
        return operationLine;
    }

    Map<HeaderType, String> getHeaders() {
        return headers;
    }

    @Nullable
    String getBody() {
        return body;
    }

    public OperationType getOperationType(){
        return operationLine.getOperationType();
    }

    public String getResourceName(){
        return operationLine.getResourceName();
    }

    public int getSequenceNumber(){
        return new Integer(headers.get(HeaderType.NSEQ));
    }

    public Address getHostAddress() throws AddressSyntaxException {
        return AddressParser.parseAddressHostAndEndpoint(headers.get(HeaderType.HOST));
    }
}
