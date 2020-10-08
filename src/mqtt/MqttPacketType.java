package mqtt;

/**
 * author blake
 * date 2020-10-01 17:15:51
 **/

public enum MqttPacketType {

    Reserved(0),
    CONNECT(1),
    CONNACK(2),
    PUBLISH(3),
    PUBACK(4),
    PUBREC(5),
    PUBREL(6),
    PUBCOMP(7),
    SUBSCRIBE(8),
    SUBACK(9),
    UNSUBSCRIBE(10),
    UNSUBACK(11),
    PINGREQ(12),
    PINGRESP(13),
    DISCONNECT(14),
    AUTH(15);

    private final int value;
    private static final MqttPacketType[] VALUES = MqttPacketType.values();

    MqttPacketType(int value) { this.value = value; }

    public int getValue() {
        return this.value;
    }

    /*
     * description:
     * author blake
     * date   2020-10-01 16:37:32
     * param: value
     * return MqttPacketType
     **/
    public static MqttPacketType valueOf(int value) {
        if (value > 0 && value < VALUES.length) {
            return VALUES[value];
        } else {
            throw new IllegalArgumentException("Illegal message type: " + value);
        }
    }
}
