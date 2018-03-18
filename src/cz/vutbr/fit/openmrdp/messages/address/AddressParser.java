package cz.vutbr.fit.openmrdp.messages.address;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;

/**
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
public final class AddressParser {

    public static Address parseAddressHostAndEndpoint(String hostAddress) throws AddressSyntaxException {
        checkCorrectSyntax(hostAddress);

        return parseHostAddress(hostAddress);
    }

    private static void checkCorrectSyntax(String hostAddress) throws AddressSyntaxException {
        if(!hostAddress.contains("/")){
            throw new AddressSyntaxException("Host Address does not have correct syntax.");
        }
    }

    private static Address parseHostAddress(String hostAddress) {
        int protocolDelimiterIndex;
        int endpointDelimiterIndex;
        String hostName;

        if(hostAddress.contains("//")){
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
}
