package cz.vutbr.fit.openmrdp.exceptions;

/**
 * @author Jiri Koudelka
 * @since 01.03.2018.
 */
public final class InformationBaseException extends RuntimeException {

    public InformationBaseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
