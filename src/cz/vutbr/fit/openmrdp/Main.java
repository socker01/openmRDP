package cz.vutbr.fit.openmrdp;

import cz.vutbr.fit.openmrdp.api.OpenmRDPServerAPI;
import cz.vutbr.fit.openmrdp.api.OpenmRDPServerAPIImpl;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.logger.MrdpTestLoggerImpl;
import cz.vutbr.fit.openmrdp.security.SecurityConfiguration;
import cz.vutbr.fit.openmrdp.security.SecurityConfigurationFactory;
import cz.vutbr.fit.openmrdp.security.UserAuthorizatorTestImpl;
import cz.vutbr.fit.openmrdp.server.AddressRetriever;
import cz.vutbr.fit.openmrdp.server.ServerConfiguration;

import java.net.SocketException;

public class Main {

    public static void main(String[] args) throws NetworkCommunicationException, SocketException {

        SecurityConfiguration securityConfiguration =
                SecurityConfigurationFactory.createSecureSecurityConfiguration(new UserAuthorizatorTestImpl(),
                        "examplekey.jks", "password");
//        SecurityConfiguration securityConfiguration = SecurityConfigurationFactory.createNonSecureSecurityConfiguration();
        ServerConfiguration serverConfiguration = new ServerConfiguration(AddressRetriever.getLocalIpAddress(), 27774, securityConfiguration);
        OpenmRDPServerAPI api = new OpenmRDPServerAPIImpl(serverConfiguration, new MrdpTestLoggerImpl());

        try {
            api.receiveMessages();
        } catch (AddressSyntaxException e) {
            e.printStackTrace();
        }
    }
}
