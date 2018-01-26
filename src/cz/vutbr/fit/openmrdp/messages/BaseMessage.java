package cz.vutbr.fit.openmrdp.messages;

import java.util.Map;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public class BaseMessage {

    private final OperationType operationType;
    private final String resource;
    private final Map<HeaderType, String> headers;
    private final String body;

    private BaseMessage(Builder builder){
        this.operationType = builder.operationType;
        this.resource = builder.resource;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public static class Builder{
        private OperationType operationType;
        private String resource;
        private Map<HeaderType, String> headers;
        private String callbackURI;
        private String body;

        public Builder operationType(OperationType operationType){
            this.operationType = operationType;
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

    public OperationType getOperationType() {
        return operationType;
    }

    public String getResource() {
        return resource;
    }

    public Map<HeaderType, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
