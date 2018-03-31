package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.address.Address;
import cz.vutbr.fit.openmrdp.messages.address.AddressParser;
import cz.vutbr.fit.openmrdp.messages.dto.ReDELResponseDTO;
import org.junit.Test;

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
            "<redel xmlns=\"http://www.awareit.com/soam/2006/04/redel\">\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.awareit.com/soam/2006/04/redel\n" +
            "http://www.awareit.com/soam/2006/04/redel.xsd\">\n" +
            "\n" +
            "<resource uri=\"urn:uuid:fuel\">\n" +
            "location url=\"urn:uuid:room/urn:uuid:box/urn:uuid:fuel\"/>\n" +
            "/resource>\n" +
            "\n" +
            "/redel>\n";

    @Test
    public void testCreateReDELMessage() throws AddressSyntaxException {
        Address address = AddressParser.parseAddressHostAndEndpoint(TEST_HOST_ADDRESS);
        BaseMessage redelMessage = MessageCreator.createReDELResponse(createReDELResponseDTO(address));

        assertThat(redelMessage.getOperationType(), is(OperationType.POST));

        assertThat(redelMessage.getHeaders().size(), is(OperationType.POST.getHeadersCount()));

        assertThat(redelMessage.getHeaders().get(HeaderType.HOST), is(address.getHostAddress()));
        assertThat(redelMessage.getHeaders().get(HeaderType.NSEQ), is(String.valueOf(TEST_SEQUENCE_NUMBER)));
        assertThat(redelMessage.getHeaders().get(HeaderType.CONTENT_TYPE), is(ContentType.REDEL.getCode()));
        assertThat(redelMessage.getHeaders().get(HeaderType.CONTENT_LENGTH), is(String.valueOf(371)));

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

    private ReDELResponseDTO createReDELResponseDTO(Address address) {

        return new ReDELResponseDTO.Builder()
                .withAddress(address)
                .withSequenceNumber(TEST_SEQUENCE_NUMBER)
                .withResourceLocation(TEST_RESOURCE_LOCATION)
                .withResourceUri(TEST_RESOURCE_NAME)
                .build();
    }

    private ReDELResponseDTO createReDELResponseWithEmptyResourceLocation(Address address){

        return new ReDELResponseDTO.Builder()
                .withAddress(address)
                .withSequenceNumber(TEST_SEQUENCE_NUMBER)
                .withResourceLocation(null)
                .withResourceUri(TEST_RESOURCE_NAME)
                .build();
    }

    private ReDELResponseDTO createReDELResponseWithEmptyResourceURI(Address address){

        return new ReDELResponseDTO.Builder()
                .withAddress(address)
                .withSequenceNumber(TEST_SEQUENCE_NUMBER)
                .withResourceLocation(TEST_RESOURCE_LOCATION)
                .withResourceUri(null)
                .build();
    }
}