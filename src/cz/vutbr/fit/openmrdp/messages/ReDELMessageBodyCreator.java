package cz.vutbr.fit.openmrdp.messages;

/**
 * @author Jiri Koudelka
 * @since 24.03.2018.
 */
final class ReDELMessageBodyCreator {

    private static final String XML_VERSION_AND_ENCODING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String REDEL_HEADER = "<redel xmlns=\"http://www.awareit.com/soam/2006/04/redel\">";
    private static final String XSI_HEADER = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
    private static final String SCHEMA_LOCATION_HEADER = "xsi:schemaLocation=\"http://www.awareit.com/soam/2006/04/redel\n" +
            "http://www.awareit.com/soam/2006/04/redel.xsd\">";

    static MessageBody createRedelMessage(String resourceUri, String resourceLocation){
        String builder = XML_VERSION_AND_ENCODING +
                "\n" +
                REDEL_HEADER +
                "\n" +
                XSI_HEADER +
                "\n" +
                SCHEMA_LOCATION_HEADER +
                "\n\n" +
                "<resource uri=\"" +
                resourceUri +
                "\">\n" +
                "location url=\"" +
                resourceLocation +
                "\"/>\n" +
                "/resource>\n\n" +
                "/redel>\n";

        return new MessageBody(builder, ContentType.REDEL);
    }
}
