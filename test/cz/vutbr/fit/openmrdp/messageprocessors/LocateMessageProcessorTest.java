package cz.vutbr.fit.openmrdp.messageprocessors;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.ContentType;
import cz.vutbr.fit.openmrdp.messages.HeaderType;
import cz.vutbr.fit.openmrdp.messages.MessageFactory;
import cz.vutbr.fit.openmrdp.model.InfoManager;
import cz.vutbr.fit.openmrdp.model.informationbase.InformationBaseTestService;
import cz.vutbr.fit.openmrdp.model.ontology.OntologyTestService;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 31.03.2018.
 */
public final class LocateMessageProcessorTest {

    private static final String TEST_CALLBACK_URI = "testCallBackURI";
    private static final String TEST_ENDPOINT = "testendpoint";
    private static final String EXISTING_RESOURCE_NAME = "urn:uuid:fuel1";
    private static final String NON_EXISTING_RESOURCE_NAME = "urn:uuid:fuel2";

    private LocateMessageProcessor locateMessageProcessor;

    @Before
    public void init() {
        InfoManager infoManager = InfoManager.getInfoManager(new InformationBaseTestService(), new OntologyTestService());
        locateMessageProcessor = new LocateMessageProcessor(infoManager);
    }

    @Test
    public void locateExistingResource() throws AddressSyntaxException {
        BaseMessage returnedMessage = locateMessageProcessor.processMessage(createLocateBaseMessage(EXISTING_RESOURCE_NAME));

        assertMessageHeaders(returnedMessage.getHeaders());
        assertThat(Integer.valueOf(returnedMessage.getHeaders().get(HeaderType.CONTENT_LENGTH)), greaterThan(0));
        assertThat(returnedMessage.getResourceName(), is(TEST_ENDPOINT));
        assertThat(returnedMessage.getMessageBody().getQuery(), is(notNullValue()));
    }

    @Test
    public void locateNonExistingResource() throws AddressSyntaxException {
        BaseMessage returnedMessage = locateMessageProcessor.processMessage(createLocateBaseMessage(NON_EXISTING_RESOURCE_NAME));

        assertMessageHeaders(returnedMessage.getHeaders());
        assertThat(Integer.valueOf(returnedMessage.getHeaders().get(HeaderType.CONTENT_LENGTH)), is(0));
        assertThat(returnedMessage.getResourceName(), is(TEST_ENDPOINT));
        assertThat(returnedMessage.getMessageBody().getQuery(), is(nullValue()));
    }

    private void assertMessageHeaders(Map<HeaderType, String> headers) {
        assertThat(headers.keySet(), hasSize(4));

        assertThat(headers.get(HeaderType.CONTENT_TYPE), is(ContentType.REDEL.getCode()));
        assertThat(headers.containsKey(HeaderType.NSEQ), is(true));
        assertThat(headers.get(HeaderType.HOST), is(TEST_CALLBACK_URI));
    }

    private BaseMessage createLocateBaseMessage(String resourceName) {
        return MessageFactory.createLocateMessage(resourceName, TEST_CALLBACK_URI + "/" + TEST_ENDPOINT);
    }
}