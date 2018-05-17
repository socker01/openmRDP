package cz.vutbr.fit.openmrdp.logger;

import com.sun.istack.internal.NotNull;

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
    void logDebug(@NotNull String message);

    /**
     * Info level log
     * @param message - message to log
     */
    void logInfo(@NotNull String message);

    /**
     * Error level log
     * @param error - error to log
     */
    void logError(@NotNull String error);
}
