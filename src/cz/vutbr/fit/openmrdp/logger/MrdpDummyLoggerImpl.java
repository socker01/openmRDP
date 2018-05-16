package cz.vutbr.fit.openmrdp.logger;

/**
 * The production implementation of the {@link MrdpLogger}.
 *
 * Use this logger if you don't want to log any information.
 *
 * @author Jiri Koudelka
 * @since 07.05.2018
 */
public final class MrdpDummyLoggerImpl implements MrdpLogger{
    @Override
    public void logDebug(String message) {
        // do nothing
    }

    @Override
    public void logInfo(String message) {
        // do nothing
    }

    @Override
    public void logError(String error) {
        // do nothing
    }
}
