package cz.vutbr.fit.openmrdp.messages;

import com.google.common.annotations.VisibleForTesting;

import java.nio.charset.Charset;

/**
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
public final class MessageBody {
    private final String query;
    private final ContentType contentType;

    @VisibleForTesting
    static final String DEFAULT_QUERY_CHARSET = "UTF-8";

    @VisibleForTesting
    public MessageBody(String query, ContentType contentType) {
        this.query = query;
        this.contentType = contentType;
    }

    public String getQuery() {
        return query;
    }

    public ContentType getContentType() {
        return contentType;
    }

    int calculateBodyLength() {
        byte[] bodyByteArray = query.getBytes(Charset.forName(DEFAULT_QUERY_CHARSET));

        return bodyByteArray.length;
    }
}
