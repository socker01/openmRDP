package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.messages.BaseMessage;

import java.io.IOException;

/**
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class MessageSenderTestImpl implements MessageSender{

    @Override
    public void sendMRDPMessage(BaseMessage message) throws IOException {
        //do nothing
    }

    @Override
    public void sendReDELMessage(BaseMessage message) {
        //do nothing
    }
}
