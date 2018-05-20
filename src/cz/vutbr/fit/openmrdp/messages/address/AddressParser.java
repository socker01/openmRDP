package cz.vutbr.fit.openmrdp.messages.address;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;

/**
 * Parser that creates {@link Address} object from {@link String} address.
 *
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
public final class AddressParser {

    /**
     * Parse {@link String} address and create appropriate {@link Address} object
     *
     * @param hostAddress - host address stored in the {@link String} object
     * @return - {@link Address}
     * @throws AddressSyntaxException - if the address syntax is not correct
     */
    @NotNull
    public static Address parseAddressHostAndEndpoint(@NotNull String hostAddress) throws AddressSyntaxException {
        checkCorrectSyntax(hostAddress);

        return parseHostAddress(hostAddress);
    }

    private static void checkCorrectSyntax(@NotNull String hostAddress) throws AddressSyntaxException {
        if (!hostAddress.contains("/")) {
            throw new AddressSyntaxException("Host Address does not have correct syntax.");
        }
    }

    @NotNull
    private static Address parseHostAddress(@NotNull String hostAddress) {
        int protocolDelimiterIndex;
        int endpointDelimiterIndex;
        String hostName;

        if (hostAddress.contains("//")) {
            protocolDelimiterIndex = hostAddress.indexOf("//");
            endpointDelimiterIndex = hostAddress.indexOf("/", protocolDelimiterIndex + 2);

            hostName = hostAddress.substring(protocolDelimiterIndex + 2, endpointDelimiterIndex);
        } else {
            endpointDelimiterIndex = hostAddress.indexOf("/");
            hostName = hostAddress.substring(0, endpointDelimiterIndex);
        }


        String endPoint = hostAddress.substring(endpointDelimiterIndex + 1);

        return new Address(hostName, endPoint);
    }

    /**
     * Parse port from address string
     *
     * @param address - address stored in the {@link String}
     * @return - {@link Integer}
     */
    @Nullable
    public static Integer parsePort(@NotNull String address) {
        address = removeProtocol(address);
        address = removeEndpoint(address);
        int delimiterIndex = address.indexOf(":");

        if (delimiterIndex != -1) {
            return Integer.parseInt(address.substring(delimiterIndex + 1));
        }

        return null;
    }

    @NotNull
    private static String removeEndpoint(@NotNull String address) {
        int endpointDelimiter = address.indexOf("/");
        if (endpointDelimiter != -1) {
            return address.substring(0, endpointDelimiter);
        }

        return address;
    }

    /**
     * Remove endpoint, port and protocol from original address
     *
     * @param address - {@link String}
     * @return - {@link String}
     */
    @NotNull
    public static String parseAddressWithoutPort(@NotNull String address) {
        address = removeProtocol(address);

        int delimiterIndex = address.indexOf(":");
        if (delimiterIndex != -1) {
            return address.substring(0, delimiterIndex);
        }

        return address;
    }

    /**
     * Parse endpoint from address string
     *
     * @param address - {@link String}
     * @return - {@link String}
     */
    @NotNull
    public static String parseEndpoint(@NotNull String address) {
        address = removeProtocol(address);

        int endpointDelimiter = address.indexOf("/");
        if (endpointDelimiter != -1) {
            return address.substring(endpointDelimiter);
        }

        return "/";

    }

    @NotNull
    private static String removeProtocol(@NotNull String address) {
        int protocolDelimiter = address.indexOf("//");
        if (protocolDelimiter != -1) {
            return address.substring(protocolDelimiter + 2);
        }

        return address;
    }
}
