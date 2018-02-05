package cz.vutbr.fit.openmrdp.messages;

/**
 * @author Jiri Koudelka
 * @since 05.02.2018.
 */
class MessageTestBase {

    protected static final String TEST_RESOURCE_NAME = "?testResource";
    protected static final String TEST_CALLBACK_URI = "192.168.0.1/testUri";
    protected static final String TEST_QUERY = "?testResource <http://www.awareit.com/onto/2005/12/locationlocatedIn> ?room";

    BaseMessage message;
}
