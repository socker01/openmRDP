package cz.vutbr.fit.openmrdp.messages;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
public enum HeaderType {
    NSEQ("NSeq"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    CALLBACK_URI("Callback-URI"),
    HOST("Host");

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

    String getHeaderCode() {
        return headerCode;
    }
}
