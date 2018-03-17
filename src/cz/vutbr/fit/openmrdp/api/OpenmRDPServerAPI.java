package cz.vutbr.fit.openmrdp.api;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public interface OpenmRDPServerAPI {

    void receiveIncomingMessages();

    void addInformationToInformationModel(String subject, String predicate, String object);
}
