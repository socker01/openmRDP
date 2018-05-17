package cz.vutbr.fit.openmrdp.messages;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public enum HeaderType {
    ACCEPT_LANGUAGE("Accept-Language"),
    ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
    AUTHORIZATION("Authorization"),
    NSEQ("NSeq"),
    CALLBACK_URI("Callback-URI"),
    CLIENT_ADDRESS("Client-Address"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    HOST("Host"),
    USER_AGENT("User-Agent");

    private final String headerCode;

    HeaderType(String headerCode) {
        this.headerCode = headerCode;
    }

    static HeaderType fromString(String headerCode){
        for (HeaderType headerType : HeaderType.values()){
            if(headerType.getHeaderCode().equals(headerCode)){

                return headerType;
            }
        }

        throw new IllegalArgumentException(String.format("There is no value with name '%s' in Enum %s", headerCode, getEnumName()));
    }

    private static String getEnumName()
    {
        return "HeaderType";
    }

    public String getHeaderCode() {
        return headerCode;
    }
}
