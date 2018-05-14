package cz.vutbr.fit.openmrdp;

import cz.vutbr.fit.openmrdp.api.OpenmRDPServerAPI;
import cz.vutbr.fit.openmrdp.api.OpenmRDPServerAPIImpl;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.logger.MrdpTestLoggerImpl;
import cz.vutbr.fit.openmrdp.security.SecurityConfiguration;
import cz.vutbr.fit.openmrdp.security.SecurityConfigurationFactory;
import cz.vutbr.fit.openmrdp.server.ServerConfiguration;

public class Main {

    public static void main(String[] args) throws NetworkCommunicationException {

//        InfoManager manager = InfoManager.getInfoManager(new InformationBaseTestService(), new OntologyProdService());
//        IdentifyMessageProcessor processor = new IdentifyMessageProcessor(manager);
//
//        String query = "?material <loc:locatedIn> ?room" + "\n" +
//                "?material rdf:type mat:inflammableThing" + "\n" +
//                "urn:uuid:drill1 <loc:locatedIn> ?room" + "\n" +
//                "urn:uuid:drill1 task:drilling ?sur" + "\n" +
//                "?sur rdf:type mat:metallicThing";
//
//        MessageBody body = new MessageBody(query, ContentType.PLANT_QUERY);
//        BaseMessage message = MessageFactory.createIdentifyMessage("?material", "test/end", body, 1);
//        try {
//            processor.processMessage(message);
//        } catch (AddressSyntaxException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("test");

//        OpenmRDPClientAPI api = new OpenmRDPClientApiImpl("testCallbackURI");

        SecurityConfiguration securityConfiguration = SecurityConfigurationFactory.createNonSecureSecurityConfiguration();
        ServerConfiguration serverConfiguration = new ServerConfiguration("192.168.1.53", 27774, securityConfiguration);
        OpenmRDPServerAPI api = new OpenmRDPServerAPIImpl(serverConfiguration, new MrdpTestLoggerImpl());

        try {
            api.receiveMessages();
        } catch (AddressSyntaxException e) {
            e.printStackTrace();
        }
    }
}
