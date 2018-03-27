package cz.vutbr.fit.openmrdp.model.processor;

import cz.vutbr.fit.openmrdp.exceptions.AddressSyntaxException;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.model.InfoManager;

/**
 * @author Jiri Koudelka
 * @since 27.03.2018.
 */
public final class IdentifyMessageProcessor implements MessageProcessor {

    private final InfoManager infoManager;

    public IdentifyMessageProcessor(InfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @Override
    public BaseMessage processMessage(BaseMessage receivedMessage) throws AddressSyntaxException {
        return null;
    }
}
