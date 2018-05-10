package cz.vutbr.fit.openmrdp.logger;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public final class MrdpTestLoggerImpl implements MrdpLogger {
    @Override
    public void logDebug(String message) {
        System.out.println("DEBUG: " + message);
    }

    @Override
    public void logInfo(String message) {
        System.out.println("INFO: " + message);
    }

    @Override
    public void logError(String error) {
        System.out.println("ERROR: " + error);
    }
}
