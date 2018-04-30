package cz.vutbr.fit.openmrdp;

import cz.vutbr.fit.openmrdp.api.OpenmRDPClientAPI;
import cz.vutbr.fit.openmrdp.api.OpenmRDPClientApiImpl;
import cz.vutbr.fit.openmrdp.api.OpenmRDPServerAPI;
import cz.vutbr.fit.openmrdp.api.OpenmRDPServerAPIImpl;
import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.messageprocessors.IdentifyMessageProcessor;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.messages.MessageFactory;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyProdService;
import cz.vutbr.fit.openmrdp.security.SecurityConfiguration;
import cz.vutbr.fit.openmrdp.server.ServerConfiguration;

import java.net.ServerSocket;

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

        SecurityConfiguration securityConfiguration = new SecurityConfiguration(false, null);
        ServerConfiguration serverConfiguration = new ServerConfiguration("127.0.0.1", 2774);
        OpenmRDPServerAPI api = new OpenmRDPServerAPIImpl(securityConfiguration, serverConfiguration);

        try {
            api.receiveMessages();
        } catch (AddressSyntaxException e) {
            e.printStackTrace();
        }
    }
}
