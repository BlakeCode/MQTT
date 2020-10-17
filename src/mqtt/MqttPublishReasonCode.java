package mqtt;

public enum MqttPublishReasonCode {

    SUCCESS((byte)0x00),
    NO_MATCHING_SUBSCRIBERS((byte)0x10),
    UNSPECIFIED_ERROR((byte)0x80),
    IMPLEMENTATION_SPECIFIC_ERROR((byte)0x83),
    NOT_AUTHORIZED((byte)0x87),
    TOPIC_NAME_INVALID((byte)0x90),
    PACKET_IDENTIFIER_IN_USE((byte)0x91),
    QUOTA_EXCEEDED((byte)0x97),
    PAYLOAD_FORMAT_INVALID((byte)0x99)
    ;

    private byte value;
    MqttPublishReasonCode(byte value) { this.value = value; }

    private static final MqttPublishReasonCode[] VALUES = MqttPublishReasonCode.values();
    public byte getValue() {
        return this.value;
    }

    /**
     * description: return reasonCode according to value
     * author blake
     * date   2020-10-17 19:21:49
     * @param: value
     * return mqtt.MqttPublishReasonCode
     **/
    public static MqttPublishReasonCode valueOf(byte value) {

        MqttPublishReasonCode reasonCode = null;
        try {
            reasonCode = VALUES[value];
        } catch (ArrayIndexOutOfBoundsException ex) {

        }

        if(reasonCode == null) {
            throw new IllegalArgumentException("unknown connect reason code: " + reasonCode);
        } else {
            return reasonCode;
        }
    }
}
