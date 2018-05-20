package cz.vutbr.fit.openmrdp.messages;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.nio.charset.Charset;

/**
 * The body of the message. This object is mostly used as a parameter of the {@link BaseMessage} class.
 *
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
public final class MessageBody {
    @Nullable
    private final String query;
    @NotNull
    private final ContentType contentType;

    @VisibleForTesting
    static final String DEFAULT_QUERY_CHARSET = "UTF-8";

    @VisibleForTesting
    public MessageBody(@Nullable String query, @NotNull ContentType contentType) {
        this.query = query;
        this.contentType = Preconditions.checkNotNull(contentType);
    }

    @Nullable
    public String getQuery() {
        return query;
    }

    @NotNull
    public ContentType getContentType() {
        return contentType;
    }

    int calculateBodyLength() {
        byte[] bodyByteArray = query.getBytes(Charset.forName(DEFAULT_QUERY_CHARSET));

        return bodyByteArray.length;
    }
}
