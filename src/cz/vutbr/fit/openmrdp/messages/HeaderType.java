package cz.vutbr.fit.openmrdp.messages;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
enum HeaderType {
    NSEQ("NSeq"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    CALLBACK_URI("Callback-URI");

    private final String headerCode;

    HeaderType(String headerCode) {
        this.headerCode = headerCode;
    }

    public String getHeaderCode() {
        return headerCode;
    }
}
