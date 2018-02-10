package cz.vutbr.fit.openmrdp.messages;

/**
 * @author Jiri Koudelka
 * @since 27.01.2018.
 */
enum MessageProtocol {
    HTTP("HTTP","1.0"),
    MRDP("mRDP","1.0");

    private final String name;
    private final String version;

    MessageProtocol(String name, String version) {
        this.name = name;
        this.version = version;
    }

    String getProtocolCode(){
        return name + "/" + version;
    }

    static MessageProtocol fromString(String protocolRaw){
        for (MessageProtocol protocol : MessageProtocol.values()){
            if(protocol.getProtocolCode().equals(protocolRaw)){

                return protocol;
            }
        }

        throw new IllegalArgumentException(String.format("There is no value '%s' in Enum %s", protocolRaw, getEnumName()));
    }

    private static String getEnumName(){
        return "MessageProtocol";
    }
}
