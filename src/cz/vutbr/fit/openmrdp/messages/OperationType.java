package cz.vutbr.fit.openmrdp.messages;

/**
 * @author Jiri Koudelka
 * @since 26.01.2018.
 */
enum OperationType {
    IDENTIFY("IDENTIFY", 4),
    LOCATE("LOCATE", 2);

    private final String code;
    private final int headersCount;

    OperationType(String code, int headersCount) {
        this.code = code;
        this.headersCount = headersCount;
    }

    static OperationType fromString(String code){
        for (OperationType type : OperationType.values()){
            if (type.getCode().equals(code)){

                return type;
            }
        }

        throw new IllegalArgumentException(String.format("There is no value with name '%s' in Enum %s", code, getEnumName()));
    }

    private static String getEnumName(){
        return "OperationType";
    }

    String getCode() {
        return code;
    }

    int getHeadersCount() {
        return headersCount;
    }
}
