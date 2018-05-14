package cz.vutbr.fit.openmrdp.messageprocessors;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.*;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyTestService;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Jiri Koudelka
 * @since 05.04.2018.
 */
public final class IdentifyMessageProcessorTest {

    private IdentifyMessageProcessor identifyMessageProcessor;

    private static final String TEST_QUERY = "?material <loc:locatedIn> ?room" + "\n" +
            "?material rdf:type mat:inflammableThing" + "\n" +
            "urn:uuid:drill1 <loc:locatedIn> ?room" + "\n" +
            "urn:uuid:drill1 task:drilling ?sur" + "\n" +
            "?sur rdf:type mat:metallicThing";

    private static final String EXPECTED_RESPONSE_MESSAGE_BODY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<redel xmlns=\"http://www.awareit.com/soam/2006/04/redel\"\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.awareit.com/soam/2006/04/redel\n" +
            "http://www.awareit.com/soam/2006/04/redel.xsd\">\n" +
            "\n" +
            "<resource uri=\"urn:uuid:fuel1\">\n" +
            "<location url=\"urn:uuid:room1/urn:uuid:box1/urn:uuid:fuel1\"/>\n" +
            "</resource>\n" +
            "\n" +
            "</redel>\n";

    @Before
    public void init(){
        InfoManager infoManager = InfoManager.getInfoManager(new InformationBaseTestService(), new OntologyTestService());
        identifyMessageProcessor = new IdentifyMessageProcessor(infoManager);
    }

    @Test
    public void processMessageAndFindResource() throws AddressSyntaxException {
        BaseMessage baseMessage = createTestBaseMessage();
        BaseMessage responseMessage = identifyMessageProcessor.processMessage(baseMessage);

        assertHeaders(responseMessage);
        assertThat(responseMessage.getMessageBody().getQuery(), is(EXPECTED_RESPONSE_MESSAGE_BODY));
    }

    private void assertHeaders(BaseMessage responseMessage) {
        assertThat(responseMessage.getHeaders().keySet().size(), is(4));
        assertThat(responseMessage.getHeaders().get(HeaderType.CONTENT_LENGTH), is(String.valueOf(377)));
        assertThat(responseMessage.getHeaders().get(HeaderType.HOST), is("testCallBackUri"));
        assertThat(responseMessage.getHeaders().get(HeaderType.CONTENT_TYPE), is(ContentType.REDEL.getCode()));
    }

    private BaseMessage createTestBaseMessage() {
        MessageBody messageBody = new MessageBody(TEST_QUERY, ContentType.PLANT_QUERY);

        return MessageFactory.createIdentifyMessage("?material", "testCallBackUri/testEndpoint", messageBody, 1);
    }

}