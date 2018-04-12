package cz.vutbr.fit.openmrdp.exceptions;

/**
 * @author Jiri Koudelka
 * @since 12.04.2018
 */
public final class OntologyException extends RuntimeException {

    public OntologyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
