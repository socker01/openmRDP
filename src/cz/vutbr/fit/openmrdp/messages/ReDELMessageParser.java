package cz.vutbr.fit.openmrdp.messages;

import com.sun.istack.internal.NotNull;

/**
 * @author Jiri Koudelka
 * @since 30.04.2018
 */
public final class ReDELMessageParser {

    public static String parseLocationFromRedelMessage(String message) {
        MessageValidator.validateReDELMessage(message);
        return getLocation(message);
    }

    @NotNull
    private static String getLocation(String message) {
        String parsedLocation = message.substring(message.indexOf(ReDELMessageBodyCreator.LOCATION_TAG) + ReDELMessageBodyCreator.LOCATION_TAG.length() + 1);

        return parsedLocation.substring(0, parsedLocation.indexOf("\"/>"));
    }
}
