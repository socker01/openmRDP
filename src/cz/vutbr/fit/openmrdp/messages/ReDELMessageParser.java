package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.NotNull;

/**
 * Parse information from ReDEL messages.
 *
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public final class ReDELMessageParser {

    @NotNull
    public static String parseLocationFromRedelMessage(@NotNull String message) {
        MessageValidator.validateReDELMessage(message);
        return getLocation(message);
    }

    @NotNull
    private static String getLocation(@NotNull String message) {
        String parsedLocation = message.substring(message.indexOf(ReDELMessageBodyFactory.LOCATION_TAG) + ReDELMessageBodyFactory.LOCATION_TAG.length() + 1);

        return parsedLocation.substring(0, parsedLocation.indexOf("\"/>"));
    }
}
