package mqtt;

public enum MqttSubscribeReasonCode {

    GRANTED_QOS_0((byte)0),
    GRANTED_QOS_1((byte)1),
    GRANTED_QOS_2((byte)2),
    UNSPECIFIED_ERROR((byte)0x80),
    IMPLEMENTATION_SPECIFIC_ERROR((byte)0x83),
    NOT_AUTHORIZED((byte)0x87),
    TOPIC_FILTER_INVALID((byte)0x8F),
    PACKET_IDENTIFIER_IN_USE((byte)0x91),
    QUOTA_EXCEEDED((byte)0x97),
    SHARED_SUBSCRIPTIONS_NOT_SUPPORTED((byte)0x9E),
    SUBSCRIPTION_IDENTIFIERS_NOT_SUPPORTED((byte)0xA1),
    WILDCARD_SUBSCRIPTIONS_NOT_SUPPORTED((byte)0xA2)
    ;

    private byte value;
    MqttSubscribeReasonCode(byte value) { this.value = value; }

    public byte getValue() { return this.value; }

    public static MqttSubscribeReasonCode valueOf(byte value) {
        for (MqttSubscribeReasonCode reason : MqttSubscribeReasonCode.values()) {
            if (reason.value == value) {
                return reason;
            }
        }
        return null;
    }

}
