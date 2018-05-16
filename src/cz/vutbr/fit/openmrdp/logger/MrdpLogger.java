package cz.vutbr.fit.openmrdp.logger;

/**
 * The interface for the logging.
 *
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public interface MrdpLogger {

    /**
     * Debug level log
     * @param message - message to log
     */
    void logDebug(String message);

    /**
     * Info level log
     * @param message - message to log
     */
    void logInfo(String message);

    /**
     * Error level log
     * @param error - error to log
     */
    void logError(String error);
}
