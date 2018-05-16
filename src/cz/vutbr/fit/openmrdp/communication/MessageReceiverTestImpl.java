package cz.vutbr.fit.openmrdp.communication;

import cz.vutbr.fit.openmrdp.messages.BaseMessage;

import java.io.IOException;

/**
 * Test iplementation of the {@link MessageReceiver}.
 *
 * This implementation is used only in tests. Do not use this implementation in the production code!
 *
 * @author Jiri Koudelka
 * @since 17.03.2018.
 */
public final class MessageReceiverTestImpl implements MessageReceiver{

    @Override
    public BaseMessage receiveMessages() throws IOException {
        return null;
    }
}
