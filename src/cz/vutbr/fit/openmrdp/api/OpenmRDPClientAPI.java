package cz.vutbr.fit.openmrdp.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import cz.vutbr.fit.openmrdp.exceptions.NetworkCommunicationException;
import cz.vutbr.fit.openmrdp.exceptions.QuerySyntaxException;

/**
 * Public client interface of OpenmRDP library
 *
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public interface OpenmRDPClientAPI {

    /**
     * Locate resource specified in parameter. This method communicate only in non-secure mode.
     *
     * @param resourceName - resource to locate
     * @return - URL with resource path
     * @throws NetworkCommunicationException - if there will be some problem with network
     */
    @Nullable
    String locateResource(@NotNull String resourceName) throws NetworkCommunicationException;

    /**
     * Locate resource specified in parameter. This method communicates only in secure mode.
     *
     * @param resourceName - resource to locate
     * @param login        - user login
     * @param password     - user password
     * @return - URL with resource path
     * @throws NetworkCommunicationException - if there will be some problem with network
     */
    @Nullable
    String locateResource(@NotNull String resourceName, @NotNull String login, @NotNull String password) throws NetworkCommunicationException;

    /**
     * Identify resource which is requested in the query parameter. This method communicates only in non-secure mode.
     *
     * @param query - query for resolve
     * @return - If the server knows the location of resolved resource, returns the resource URL.
     * If not, the server returns the name of the resolved resource.
     * @throws QuerySyntaxException          - if the query haven't expected syntax
     * @throws NetworkCommunicationException - if there will be some problem with network
     */
    @Nullable
    String identifyResource(@NotNull String query) throws QuerySyntaxException, NetworkCommunicationException;

    /**
     * Identify resource which is requested in the query parameter. This method communicates only in non-secure mode.
     *
     * @param query    - query for resolve
     * @param login    - user login
     * @param password - user password
     * @return - If the server knows the location of resolved resource, returns the resource URL.
     * If not, the server returns the name of the resolved resource.
     * @throws QuerySyntaxException          - if the query haven't expected syntax
     * @throws NetworkCommunicationException - if there will be some problem with network
     */
    @Nullable
    String identifyResource(@NotNull String query, @NotNull String login, @NotNull String password)
            throws QuerySyntaxException, NetworkCommunicationException;
}
