package cz.vutbr.fit.openmrdp.messages.address;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Executable;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
final class AddressParserTest {

    private final String TEST_HOST_ADDRESS_WITH_PROTOCOL = "http://192.168.0.1/testendpoint";
    private final String TEST_HOST_ADDRESS_WITHOUT_PROTOCOL = "192.168.0.1/testendpoint";
    private final String INCORRECT_HOST_ADDRESS = "endpointaddress";

    @Test
    void parseAddressWithProtocol() throws AddressSyntaxException {
        Address address = AddressParser.parseAddressHostAndEndpoint(TEST_HOST_ADDRESS_WITH_PROTOCOL);

        assertThat(address.getHostAddress(), is("192.168.0.1"));
        assertThat(address.getEndPoint(), is("testendpoint"));
    }

    @Test
    void parseAddressWithoutProtocol() throws AddressSyntaxException {
        Address address = AddressParser.parseAddressHostAndEndpoint(TEST_HOST_ADDRESS_WITHOUT_PROTOCOL);

        assertThat(address.getHostAddress(), is("192.168.0.1"));
        assertThat(address.getEndPoint(), is("testendpoint"));
    }

    @Test
    void parseIncorrectAddress() throws AddressSyntaxException {
        assertThrows(AddressSyntaxException.class, () -> {AddressParser.parseAddressHostAndEndpoint(INCORRECT_HOST_ADDRESS);});
    }

}