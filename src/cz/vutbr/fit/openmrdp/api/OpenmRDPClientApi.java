package cz.vutbr.fit.openmrdp.api;

import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public interface OpenmRDPClientApi {

    String locateResource(String resourceName) throws NetworkCommunicationException;
}
