package cz.vutbr.fit.openmrdp.messages.address;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
final class AddressParserTest {

    private final String INCORRECT_HOST_ADDRESS = "endpointaddress";

    @Test
    void parseAddressWithProtocol() throws AddressSyntaxException {
        String testHostAddressWithProtocol = "http://192.168.0.1/testendpoint";
        Address address = AddressParser.parseAddressHostAndEndpoint(testHostAddressWithProtocol);

        assertThat(address.getHostAddress(), is("192.168.0.1"));
        assertThat(address.getEndPoint(), is("testendpoint"));
    }

    @Test
    void parseAddressWithoutProtocol() throws AddressSyntaxException {
        String testHostAddressWithoutProtocol = "192.168.0.1/testendpoint";
        Address address = AddressParser.parseAddressHostAndEndpoint(testHostAddressWithoutProtocol);

        assertThat(address.getHostAddress(), is("192.168.0.1"));
        assertThat(address.getEndPoint(), is("testendpoint"));
    }

    @Test
    void parseIncorrectAddress() throws AddressSyntaxException {
        assertThrows(AddressSyntaxException.class, () -> AddressParser.parseAddressHostAndEndpoint(INCORRECT_HOST_ADDRESS));
    }

}