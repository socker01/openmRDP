package cz.vutbr.fit.openmrdp.exceptions;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
public class NetworkCommunicationException extends Exception {

    public NetworkCommunicationException(String message, Throwable throwable){
        super(message, throwable);
    }
}
