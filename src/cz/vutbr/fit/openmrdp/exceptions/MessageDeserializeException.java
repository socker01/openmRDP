package cz.vutbr.fit.openmrdp.exceptions;

/**
 * @author Jiri Koudelka
 * @since 10.02.2018.
 */
public final class MessageDeserializeException extends RuntimeException {

    public MessageDeserializeException(String message) {
        super(message);
    }
}
