package cz.vutbr.fit.openmrdp;

import cz.vutbr.fit.openmrdp.messages.BaseMessage;
import cz.vutbr.fit.openmrdp.messages.MessageCreator;

public class Main {

    public static void main(String[] args) {

        BaseMessage message = MessageCreator.createLocateMessage("testResourceName", "testCallBackUri");

    }
}
