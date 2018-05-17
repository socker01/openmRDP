package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.model.base.Resource;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ReDELMessageBodyFactoryTest {

    private static final String EXPECTED_REDEL_MESSAGE_BODY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<redel xmlns=\"http://www.awareit.com/soam/2006/04/redel\"\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:schemaLocation=\"http://www.awareit.com/soam/2006/04/redel\n" +
            "http://www.awareit.com/soam/2006/04/redel.xsd\">\n" +
            "\n" +
            "<resource uri=\"urn:uuid:fuel1\">\n" +
            "<location url=\"urn:uuid:room1/\"/>\n" +
            "</resource>\n" +
            "\n" +
            "</redel>\n";

    @Test
    public void createReDELMessageBody(){
        Resource testResource = new Resource("urn:uuid:fuel1", "urn:uuid:room1/");

        MessageBody redelMessageBody = ReDELMessageBodyFactory.createRedelMessage(Collections.singletonList(testResource));

        assertThat(redelMessageBody.getContentType(), is(ContentType.REDEL));
        assertThat(redelMessageBody.getQuery(), is(EXPECTED_REDEL_MESSAGE_BODY));
        assertThat(redelMessageBody.calculateBodyLength(), is(349));
    }

}