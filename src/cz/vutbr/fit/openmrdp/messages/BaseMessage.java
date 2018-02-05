package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.Nullable;

import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public class BaseMessage {

    private final OperationLine operationLine;
    private final Map<HeaderType, String> headers;
    private final String body;

    BaseMessage(OperationLine operationLine, Map<HeaderType, String> headers, String body){
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
}
