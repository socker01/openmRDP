package cz.vutbr.fit.openmrdp.messages.address;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Jiri Koudelka
 * @since 18.03.2018.
 */
public final class AddressParserTest {

    @Test
    public void parseAddressWithProtocol() throws AddressSyntaxException {
        String testHostAddressWithProtocol = "http://192.168.0.1/testendpoint";
        Address address = AddressParser.parseAddressHostAndEndpoint(testHostAddressWithProtocol);

        assertThat(address.getHostAddress(), is("192.168.0.1"));
        assertThat(address.getEndPoint(), is("testendpoint"));
    }

    @Test
    public void parseAddressWithoutProtocol() throws AddressSyntaxException {
        String testHostAddressWithoutProtocol = "192.168.0.1/testendpoint";
        Address address = AddressParser.parseAddressHostAndEndpoint(testHostAddressWithoutProtocol);

        assertThat(address.getHostAddress(), is("192.168.0.1"));
        assertThat(address.getEndPoint(), is("testendpoint"));
    }

    @Test(expected = AddressSyntaxException.class)
    public void parseIncorrectAddress() throws AddressSyntaxException {
        String INCORRECT_HOST_ADDRESS = "endpointaddress";
        AddressParser.parseAddressHostAndEndpoint(INCORRECT_HOST_ADDRESS);
    }

}