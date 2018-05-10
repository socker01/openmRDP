package cz.vutbr.fit.openmrdp.messages;

import cz.vutbr.fit.openmrdp.model.base.Resource;

import java.util.List;

/**
 * @author Jiri Koudelka
 * @since 24.03.2018.
 */
final class ReDELMessageBodyCreator {

    private static final String XML_VERSION_AND_ENCODING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String REDEL_HEADER = "<redel xmlns=\"http://www.awareit.com/soam/2006/04/redel\"";
    private static final String XSI_HEADER = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
    private static final String SCHEMA_LOCATION_HEADER = "xsi:schemaLocation=\"http://www.awareit.com/soam/2006/04/redel\n" +
            "http://www.awareit.com/soam/2006/04/redel.xsd\">";
    private static final String RESOURCE_TAG = "<resource uri";
    static final String LOCATION_TAG = "<location url=";

    static MessageBody createRedelMessage(List<Resource> resources) {
        StringBuilder builder = new StringBuilder();

        if (!resources.isEmpty()){
            builder.append(XML_VERSION_AND_ENCODING +
                    "\n" +
                    REDEL_HEADER +
                    "\n" +
                    XSI_HEADER +
                    "\n" +
                    SCHEMA_LOCATION_HEADER);
        }

        for (Resource resource : resources){
            if (resource.getResourceUri() != null && resource.getResourceLocation() != null) {
                builder.append("\n\n" + "<resource uri=\"")
                        .append(resource.getResourceUri())
                        .append("\">\n")
                        .append("<location url=\"")
                        .append(resource.getResourceLocation())
                        .append("\"/>\n")
                        .append("</resource>");
            }
        }

        if (!resources.isEmpty()){
            builder.append("\n\n</redel>\n");
        }

        String finalMessage =  null;

        if(hasResourceInformation(builder.toString())){
            finalMessage = builder.toString();
        }

        return new MessageBody(finalMessage, ContentType.REDEL);
    }

    private static boolean hasResourceInformation(String finalMessage){
        return finalMessage.contains(RESOURCE_TAG);
    }
}
