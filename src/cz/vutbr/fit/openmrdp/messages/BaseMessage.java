package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.Nullable;

import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public class BaseMessage {

    private final OperationLine operationLine;
    private final String resource;
    private final Map<HeaderType, String> headers;
    private final String body;

    private BaseMessage(Builder builder){
        this.operationLine = builder.operationLine;
        this.resource = builder.resource;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public static class Builder{
        private OperationLine operationLine;
        private String resource;
        private Map<HeaderType, String> headers;
        private String body;

        public Builder operationType(OperationLine operationLine){
            this.operationLine = operationLine;
            return this;
        }

        public Builder resource(String resource){
            this.resource = resource;
            return this;
        }

        public Builder headers(Map<HeaderType, String> headers){
            this.headers = headers;
            return this;
        }

        public Builder body(String body){
            this.body = body;
            return this;
        }

        public BaseMessage build(){
            return new BaseMessage(this);
        }
    }

    public OperationLine getOperationLine() {
        return operationLine;
    }

    public String getResource() {
        return resource;
    }

    public Map<HeaderType, String> getHeaders() {
        return headers;
    }

    @Nullable
    public String getBody() {
        return body;
    }
}
