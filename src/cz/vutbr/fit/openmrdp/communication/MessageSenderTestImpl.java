package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.address.Address;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class MessageSenderTestImpl implements MessageSender {

    @Override
    public void sendMRDPMessage(BaseMessage message) {
        System.out.println("mRDP message was sent.");
    }

    @Override
    public void sendReDELMessage(BaseMessage message) {
        System.out.println("ReDEL message was sent.");
    }

    @Override
    public void sendInformationAboutConnection(Address clientAddress, String message) {
        System.out.println("Non-secure connection info message was sent.");
    }
}
