package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.address.Address;
import cz.vutbr.fit.openmrdp.messages.address.AddressParser;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import cz.vutbr.fit.openmrdp.model.base.Resource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 24.03.2018.
 */
public final class RedelMessageCreatorTest {

    private static final String TEST_HOST_ADDRESS = "http://169.254.0.2/testendpoint";
    private static final int TEST_SEQUENCE_NUMBER = 15;
    private static final String TEST_RESOURCE_LOCATION = "urn:uuid:room/urn:uuid:box/urn:uuid:fuel";
    private static final String TEST_RESOURCE_NAME = "urn:uuid:fuel";

    private static final String EXPECTED_MESSAGE_BODY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<redel xmlns=\"http://www.awareit.com/soam/2006/04/redel\"\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.awareit.com/soam/2006/04/redel\n" +
            "http://www.awareit.com/soam/2006/04/redel.xsd\">\n" +
            "\n" +
            "<resource uri=\"urn:uuid:fuel\">\n" +
            "<location url=\"urn:uuid:room/urn:uuid:box/urn:uuid:fuel\"/>\n" +
            "</resource>\n" +
            "\n" +
            "</redel>\n";

    private static final String EXPECTED_MESSAGE_BODY_WITH_TWO_WARIABLES = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<redel xmlns=\"http://www.awareit.com/soam/2006/04/redel\"\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.awareit.com/soam/2006/04/redel\n" +
            "http://www.awareit.com/soam/2006/04/redel.xsd\">\n" +
            "\n" +
            "<resource uri=\"urn:uuid:fuel\">\n" +
            "<location url=\"urn:uuid:room/urn:uuid:box/urn:uuid:fuel\"/>\n" +
            "</resource>\n" +
            "\n" +
            "<resource uri=\"urn:uuid:fuel\">\n" +
            "<location url=\"urn:uuid:room/urn:uuid:box/urn:uuid:fuel\"/>\n" +
            "</resource>\n" +
            "\n" +
            "</redel>\n";

    @Test
    public void testCreateReDELMessage() throws AddressSyntaxException {
        Address address = AddressParser.parseAddressHostAndEndpoint(TEST_HOST_ADDRESS);
        BaseMessage redelMessage = MessageCreator.createReDELResponse(createReDELResponseDTO(address));

        assertThat(redelMessage.getOperationType(), is(OperationType.POST));

        assertThat(redelMessage.getHeaders().size(), is(OperationType.POST.getHeadersCount()));

        assertThat(redelMessage.getHeaders().get(HeaderType.HOST), is(address.getHostAddress()));
        assertThat(redelMessage.getHeaders().get(HeaderType.NSEQ), is(String.valueOf(TEST_SEQUENCE_NUMBER)));
        assertThat(redelMessage.getHeaders().get(HeaderType.CONTENT_TYPE), is(ContentType.REDEL.getCode()));
        assertThat(redelMessage.getHeaders().get(HeaderType.CONTENT_LENGTH), is(String.valueOf(373)));

        assertThat(redelMessage.getBodyQuery(), is(EXPECTED_MESSAGE_BODY));
    }

    @Test
    public void createReDELMessageWithEmptyResourceLocation() throws AddressSyntaxException {
        Address address = AddressParser.parseAddressHostAndEndpoint(TEST_HOST_ADDRESS);
        BaseMessage redelMessage = MessageCreator.createReDELResponse(createReDELResponseWithEmptyResourceLocation(address));

        assertThat(redelMessage.getHeaders().get(HeaderType.CONTENT_LENGTH), is(String.valueOf(0)));
        assertThat(redelMessage.getBodyQuery(), is(nullValue()));
    }

    @Test
    public void createReDELMessageWithEmptyResourceURI() throws AddressSyntaxException {
        Address address = AddressParser.parseAddressHostAndEndpoint(TEST_HOST_ADDRESS);
        BaseMessage redelMessage = MessageCreator.createReDELResponse(createReDELResponseWithEmptyResourceURI(address));

        assertThat(redelMessage.getHeaders().get(HeaderType.CONTENT_LENGTH), is(String.valueOf(0)));
        assertThat(redelMessage.getBodyQuery(), is(nullValue()));
    }

    @Test
    public void createReDELMessageWithTwoResources() throws AddressSyntaxException {
        Address address = AddressParser.parseAddressHostAndEndpoint(TEST_HOST_ADDRESS);
        BaseMessage redelMessage = MessageCreator.createReDELResponse(createReDELResponseWithTwoResources(address));

        assertThat(redelMessage.getHeaders().get(HeaderType.CONTENT_LENGTH), is(String.valueOf(476)));
        assertThat(redelMessage.getBodyQuery(), is(EXPECTED_MESSAGE_BODY_WITH_TWO_WARIABLES));
    }

    private ReDELResponseDTO createReDELResponseDTO(Address address) {
        Resource resource = new Resource(TEST_RESOURCE_NAME, TEST_RESOURCE_LOCATION);

        return new ReDELResponseDTO.Builder()
                .withAddress(address)
                .withSequenceNumber(TEST_SEQUENCE_NUMBER)
                .withResource(Collections.singletonList(resource))
                .build();
    }

    private ReDELResponseDTO createReDELResponseWithEmptyResourceLocation(Address address){
        Resource resource = new Resource(TEST_RESOURCE_NAME, null);

        return new ReDELResponseDTO.Builder()
                .withAddress(address)
                .withSequenceNumber(TEST_SEQUENCE_NUMBER)
                .withResource(Collections.singletonList(resource))
                .build();
    }

    private ReDELResponseDTO createReDELResponseWithEmptyResourceURI(Address address){
        Resource resource = new Resource(null, TEST_RESOURCE_LOCATION);

        return new ReDELResponseDTO.Builder()
                .withAddress(address)
                .withSequenceNumber(TEST_SEQUENCE_NUMBER)
                .withResource(Collections.singletonList(resource))
                .build();
    }

    private ReDELResponseDTO createReDELResponseWithTwoResources(Address address){
        List<Resource> resources = getResourcesList();

        return new ReDELResponseDTO.Builder()
                .withAddress(address)
                .withSequenceNumber(TEST_SEQUENCE_NUMBER)
                .withResource(resources)
                .build();

    }

    private List<Resource> getResourcesList() {
        Resource resource1 = new Resource(TEST_RESOURCE_NAME, TEST_RESOURCE_LOCATION);
        Resource resource2 = new Resource(TEST_RESOURCE_NAME, TEST_RESOURCE_LOCATION);

        List<Resource> resources = new ArrayList<>();
        resources.add(resource1);
        resources.add(resource2);

        return resources;
    }
}