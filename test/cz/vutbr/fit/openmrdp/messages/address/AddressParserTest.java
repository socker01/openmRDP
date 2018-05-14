package cz.vutbr.fit.openmrdp.messages.address;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
public final class AddressParserTest {

    private static final String HOST_ADDRESS_WITH_PROTOCOL = "http://192.168.0.1/testendpoint";
    private static final String HOST_ADDRESS_WITHOUT_PROTOCOL = "192.168.0.1/testendpoint";
    private static final String HOST_ADDRESS_WITH_PORT = "http://192.168.0.1:27773/testendpoint";
    private static final String HOST_ADDRESS_WITH_PORT_WITHOUT_PROTOCOL = "192.168.0.1:27773/testendpoint";
    private static final String HOST_ADDRESS_WITHOUT_ENDPOINT = "http://192.168.0.1:27773/";
    private static final String TEST_ENDPOINT = "testendpoint";
    private static final String TEST_IP_ADDRESS = "192.168.0.1";

    @Test
    public void parseAddressWithProtocol() throws AddressSyntaxException {
        Address address = AddressParser.parseAddressHostAndEndpoint(HOST_ADDRESS_WITH_PROTOCOL);

        assertThat(address.getHostAddress(), is(TEST_IP_ADDRESS));
        assertThat(address.getEndPoint(), is(TEST_ENDPOINT));
    }

    @Test
    public void parseAddressWithoutProtocol() throws AddressSyntaxException {
        Address address = AddressParser.parseAddressHostAndEndpoint(HOST_ADDRESS_WITHOUT_PROTOCOL);

        assertThat(address.getHostAddress(), is(TEST_IP_ADDRESS));
        assertThat(address.getEndPoint(), is(TEST_ENDPOINT));
    }

    @Test(expected = AddressSyntaxException.class)
    public void parseIncorrectAddress() throws AddressSyntaxException {
        String INCORRECT_HOST_ADDRESS = "endpointaddress";
        AddressParser.parseAddressHostAndEndpoint(INCORRECT_HOST_ADDRESS);
    }

    @Test
    public void parseAddressWithPortAndProtocol(){
        testAddressParsing(HOST_ADDRESS_WITH_PORT);
        testAddressParsing(HOST_ADDRESS_WITH_PORT_WITHOUT_PROTOCOL);
    }

    private void testAddressParsing(String address){
        Integer parsedPort = AddressParser.parsePort(address);
        assertThat(parsedPort, is(27773));

        String endpoint = AddressParser.parseEndpoint(address);
        assertThat(endpoint, is("/" + TEST_ENDPOINT));

        String addressWithoutPort = AddressParser.parseAddressWithoutPort(address);
        assertThat(addressWithoutPort, is(TEST_IP_ADDRESS));
    }

    @Test
    public void parseAddressWithoutEndpoint(){
        String endpoint = AddressParser.parseEndpoint(HOST_ADDRESS_WITHOUT_ENDPOINT);
        assertThat(endpoint, is("/"));
    }

    @Test
    public void testParseAddressWithoutPort(){
        Integer parsedPort = AddressParser.parsePort(HOST_ADDRESS_WITH_PROTOCOL);
        assertThat(parsedPort, is(nullValue()));
    }

}