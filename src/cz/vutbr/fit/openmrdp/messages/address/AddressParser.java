package cz.vutbr.fit.openmrdp.messages.address;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
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

    @Nullable
    public static Integer parsePort(@NotNull String address){
        address = removeProtocol(address);
        address = removeEndpoint(address);
        int delimiterIndex = address.indexOf(":");

        if(delimiterIndex != -1){
            return Integer.parseInt(address.substring(delimiterIndex + 1));
        }

        return null;
    }

    private static String removeEndpoint(String address) {
        int endpointDelimiter = address.indexOf("/");
        if (endpointDelimiter != -1){
            return address.substring(0, endpointDelimiter);
        }

        return address;
    }

    public static String parseAddressWithoutPort(@NotNull String address){
        address = removeProtocol(address);
        System.out.println("addressWithoutProtocol: " + address);

        int delimiterIndex = address.indexOf(":");
        if (delimiterIndex != -1){
            return address.substring(0, delimiterIndex);
        }

        return address;
    }

    public static String parseEndpoint(@NotNull String address){
        address = removeProtocol(address);

        int endpointDelimiter = address.indexOf("/");
        if (endpointDelimiter != -1){
            return address.substring(endpointDelimiter);
        }

        return "/";

    }

    private static String removeProtocol(@NotNull String address) {
        int protocolDelimiter = address.indexOf("//");
        if (protocolDelimiter != -1){
            return address.substring(protocolDelimiter + 2);
        }

        return address;
    }
}
