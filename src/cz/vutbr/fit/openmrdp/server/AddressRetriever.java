package cz.vutbr.fit.openmrdp.server;

import java.net.*;
import java.util.Enumeration;

/**
 * @author Jiri Koudelka
 * @since 10.05.2018
 */
public final class AddressRetriever {

    public static String getLocalIpAddress() throws SocketException {
        String resultIpv6 = "";
        String resultIpv4 = "";

        for (Enumeration en = NetworkInterface.getNetworkInterfaces();
             en.hasMoreElements(); ) {

            NetworkInterface intf = (NetworkInterface) en.nextElement();
            for (Enumeration enumIpAddr = intf.getInetAddresses();
                 enumIpAddr.hasMoreElements(); ) {

                InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    if (inetAddress instanceof Inet4Address) {
                        resultIpv4 = inetAddress.getHostAddress();
                    } else if (inetAddress instanceof Inet6Address) {
                        resultIpv6 = inetAddress.getHostAddress();
                    }
                }
            }
        }

        return ((resultIpv4.length() > 0) ? resultIpv4 : resultIpv6);
    }
}
