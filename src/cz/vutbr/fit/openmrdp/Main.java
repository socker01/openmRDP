package cz.vutbr.fit.openmrdp;

import cz.vutbr.fit.openmrdp.communication.MessageSender;
import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageCreator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        BaseMessage message = MessageCreator.createLocateMessage("testResourceName", "testCallBackUri");

        try {
            MessageSender.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
