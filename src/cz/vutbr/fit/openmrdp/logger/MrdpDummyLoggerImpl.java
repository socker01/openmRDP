package cz.vutbr.fit.openmrdp.logger;

import com.sun.istack.internal.Nullable;

/**
 * The production implementation of the {@link MrdpLogger}.
 * <p>
 * Use this logger if you don't want to log any information.
 *
 * @author Jiri Koudelka
 * @since 07.05.2018
 */
public final class MrdpDummyLoggerImpl implements MrdpLogger {
    @Override
    public void logDebug(@Nullable String message) {
        // do nothing
    }

    @Override
    public void logInfo(@Nullable String message) {
        // do nothing
    }

    @Override
    public void logError(@Nullable String error) {
        // do nothing
    }
}
