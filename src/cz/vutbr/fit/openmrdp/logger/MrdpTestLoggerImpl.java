package cz.vutbr.fit.openmrdp.logger;

import com.sun.istack.internal.NotNull;

/**
 * The test implementation of the {@link MrdpLogger} interface.
 *
 * Use this implementation if you want to log every message into the standard output
 *
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public final class MrdpTestLoggerImpl implements MrdpLogger {
    @Override
    public void logDebug(@NotNull String message) {
        System.out.println("DEBUG: " + message);
    }

    @Override
    public void logInfo(@NotNull String message) {
        System.out.println("INFO: " + message);
    }

    @Override
    public void logError(@NotNull String error) {
        System.out.println("ERROR: " + error);
    }
}
