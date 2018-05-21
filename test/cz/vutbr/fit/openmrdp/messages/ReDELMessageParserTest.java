package cz.vutbr.fit.openmrdp.messages;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReDELMessageParserTest {

    @Test
    public void parseLocationFromRedelMessage() {
        String location = ReDELMessageParser.parseLocationFromRedelMessage(RedelMessageFactoryTest.EXPECTED_MESSAGE_BODY);

        assertThat(location, is("urn:uuid:room/urn:uuid:box/urn:uuid:fuel"));
    }

}