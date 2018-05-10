package cz.vutbr.fit.openmrdp.logger;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public interface MrdpLogger {
    void logDebug(String message);

    void logInfo(String message);

    void logError(String error);
}
