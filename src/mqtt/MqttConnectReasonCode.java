package mqtt;

/**
 * @author blake
 * date 2020-10-02 15:08:51
 **/

public enum MqttConnectReasonCode {

    SUCCESS((byte)0x00),

    UNSPECIFIED_ERROR((byte)0x80),
    MALFORMED_PACKET((byte)0X81),
    PROTOCOL_ERROR((byte)0X82),
    IMPLEMENTATION_SPECIFIC_ERROR((byte)0X83),
    UNSUPPORTED_PROTOCOL_VERSION((byte)0X84),
    CLIENT_IDENTIFIER_NOT_VALID((byte)0X85),
    BAD_USER_NAME_OR_PASSWORD((byte)0X86),
    NOT_AuthorIZED((byte)0X87),
    SERVER_UNAVAILABLE((byte)0X88),
    SERVER_BUSY((byte)0X89),
    BANNED((byte)0X8A),
    BAD_AUTHENTICATION_METHOD((byte)0X8C),
    TOPIC_NAME_INVALID((byte)0X90),
    PACKET_TOO_LARGE((byte)0X95),
    QUOTA_EXCEEDED((byte)0X97),
    PAYLOAD_FORMAT_INVALID((byte)0X99),
    RETAIN_NOT_SUPPORTED((byte)0X9A),
    QOS_NOT_SUPPORTED((byte)0X9B),
    USE_ANOTHER_SERVER((byte)0X9C),
    SERVER_MOVED((byte)0X9D),
    CONNECTION_RATE_EXCEEDED((byte)0X9F);

    private byte value;
    MqttConnectReasonCode(byte value) { this.value = value;}

    public byte getValue() {
        return this.value;
    }

    /**
     * description: return Key by Value
     * @author blake
     * date 2020-10-02 15:58:46
     * @param value
     * @return MqttConnectReasonCode
     **/

    public static MqttConnectReasonCode valueOf(byte value) {

        for (MqttConnectReasonCode reasonCode : MqttConnectReasonCode.values()) {
            if (reasonCode.value == value) {
                return reasonCode;
            }
        }
        return null;
    }
}
