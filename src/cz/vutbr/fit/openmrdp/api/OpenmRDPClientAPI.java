package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public interface OpenmRDPClientAPI {

    String locateResource(String resourceName) throws NetworkCommunicationException;

    String locateResource(String resourceName, String login, String password) throws NetworkCommunicationException;

    String identifyResource(String query) throws QuerySyntaxException, NetworkCommunicationException;

    String identifyResource(String query, String login, String password) throws QuerySyntaxException, NetworkCommunicationException;
}
