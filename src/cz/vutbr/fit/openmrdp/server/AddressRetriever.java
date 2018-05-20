package cz.vutbr.fit.openmrdp.server;

import com.sun.istack.internal.NotNull;

import java.net.*;
import java.util.Enumeration;

/**
 * AddressRetriever gets the correct local IP address of machine.
 *
 * @author Jiri Koudelka
 * @since 10.05.2018
 */
public final class AddressRetriever {

    /**
     * Get local IP address
     *
     * @return - IP address of local machine
     * @throws SocketException - if there will be some problem with network
     */
    @NotNull
    public static String getLocalIpAddress() throws SocketException {
        String resultIpv6 = "";
        String resultIpv4 = "";

        for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {

            NetworkInterface networkInterface = (NetworkInterface) en.nextElement();

            for (Enumeration interfaceInetAddresses = networkInterface.getInetAddresses();
                 interfaceInetAddresses.hasMoreElements(); ) {

                InetAddress inetAddress = (InetAddress) interfaceInetAddresses.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    if (inetAddress instanceof Inet4Address) {
                        resultIpv4 = inetAddress.getHostAddress();
                    } else if (inetAddress instanceof Inet6Address) {
                        resultIpv6 = inetAddress.getHostAddress();
                    }
                }
            }
        }

        return resultIpv4.length() > 0 ? resultIpv4 : resultIpv6;
    }
}
