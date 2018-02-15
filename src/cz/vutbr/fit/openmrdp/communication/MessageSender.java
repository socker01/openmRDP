package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.messages.BaseMessage;

import java.io.IOException;

/**
 * @author Jiri Koudelka
 * @since 15.02.2018.
 */
interface MessageSender {

    void sendMessage(BaseMessage message) throws IOException;
}
