package mqtt;

public enum MqttPublishReasonCode {

    // PUBACK Reason Code
    // PUBREC Reason Code (QoS 2 delivery part 1)
    SUCCESS((byte)0x00),
    NO_MATCHING_SUBSCRIBERS((byte)0x10),
    UNSPECIFIED_ERROR((byte)0x80),
    IMPLEMENTATION_SPECIFIC_ERROR((byte)0x83),
    NOT_AuthorIZED((byte)0x87),
    TOPIC_NAME_INVALID((byte)0x90),
    PACKET_IDENTIFIER_IN_USE((byte)0x91),
    QUOTA_EXCEEDED((byte)0x97),
    PAYLOAD_FORMAT_INVALID((byte)0x99),

    // PUBREL Reason Code
    // PUCOMP Reason Code
    // Only SUCCESS(0) and
    PACKET_IDENTIFIER_NOT_FOUND((byte)0x92)
    ;

    private byte value;
    MqttPublishReasonCode(byte value) { this.value = value; }

    public byte getValue() { return this.value; }

    /**
     * description: return Key by Value
     * @author blake
     * date   2020-10-19 15:47:53
     * @param value
     * @return mqtt.MqttPublishReasonCode
     **/
    public static MqttPublishReasonCode valueOf(byte value) {
        for (MqttPublishReasonCode reasonCode : MqttPublishReasonCode.values()) {
            if(reasonCode.value == value) {
                return reasonCode;
            }
        }
        return null;
    }

}
