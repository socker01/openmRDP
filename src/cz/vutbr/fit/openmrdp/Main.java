package cz.vutbr.fit.openmrdp;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.messages.MessageBody;
import cz.vutbr.fit.openmrdp.messages.MessageFactory;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.processors.IdentifyMessageProcessor;

public class Main {

    public static void main(String[] args) {

        InfoManager manager = new InfoManager(new InformationBaseTestService());
        IdentifyMessageProcessor processor = new IdentifyMessageProcessor(manager);

        String query = "?material <loc:locatedIn> ?room" + "\n" +
                "?material rdf:type mat:inflammableThing" + "\n" +
                "urn:uuid:drill1 <loc:locatedIn> ?room" + "\n" +
                "urn:uuid:drill1 task:drilling ?sur" + "\n" +
                "?sur rdf:type mat:metallicThing";

        MessageBody body = new MessageBody(query, ContentType.PLANT_QUERY);
        BaseMessage message = MessageFactory.createIdentifyMessage("?material", "test/end", body);
        try {
            processor.processMessage(message);
        } catch (AddressSyntaxException e) {
            e.printStackTrace();
        }

        System.out.println("test");
    }
}
