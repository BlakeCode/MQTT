package mqtt;

/**
 * author blake
 * date 2020-10-02 10:32:35
 **/

public enum MqttPropertyType {

    PAYLOAD_FORMAT_INDICATOR(1),        // Byte                 PUBLISH, Will Properties
    MESSAGE_EXPIRY_INTERVAL(2),         // Four Byte Int        PUBLISH, Will Properties
    CONTENT_TYPE(3),                    // UTF-8 String         PUBLISH, Will Properties
    RESPONSE_TOPIC(8),                  // UTF-8 String         PUBLISH, Will Properties
    CORRELATION_DATA(9),                // Byte[]               PUBLISH, Will Properties
    SUBSCRIPTION_IDENTIFIER(11),        // Variable Byte Int    PUBLISH, SUBSCRIBE
    SESSION_EXPIRY_INTERVAL(17),        // Four Byte Int        CONNECT, CONNACK, DISCONNECT
    ASSIGNED_CLIENT_IDENTIFIER(18),     // UTF-8 String         CONNACK
    SERVER_KEEP_ALIVE(19),             // Two Byte Int         CONNACK
    AUTHENTICATION_METHORD(21),         // UTF-8 String         CONNECT, CONNACK, AUTH
    AUTHENTICATION_DATA(22),            // Byte[]               CONNECT, CONNACK, AUTH
    REQUESR_PROBLEM_INFORMATION(23),    // Byte                 CONNECT
    WILL_DELAY_INTERVAL(24),            // Four Byte Int        Will Properties
    REQUEST_RESPONSE_INFORMATION(25),   // Byte                 CONNECT
    RESPONSE_INFORMATION(26),           // UTF-8 String         CONNACK
    SERVER_REFERENCE(28),               // UTF-8 String         CONNACK, DISCONNECT
    REASON_STRING(31),                  // UTF-8 String         CONNACK, PUBACK, PUBREC, PUBREL, PUBCOMP,
                                        //                      SUBACK, UNSUBACK, DISCONNECT, AUTH
    RECEIVE_MAXIMUM(33),                // Two Byte Int         CONNECT, CONNACK
    TOPIC_ALIAS_MAXIMUM(34),            // Two Byte Int         CONNECT, CONNACK
    TOPIC_ALIAS(35),                    // Two Byte Int         PUBLISH
    MAXIMUM_QOS(36),                    // Byte                 CONNACK
    RETAIN_AVAILABLE(37),               // Byte                 CONNACK
    USER_PROPERTY(38),                  // UTF-8 String         CONNECT, CONNACK, PUBLISH, Will Properties, PUBACK,
                                        //                      PUBREC, PUBREL, PUBCOMP, SUBSCRIBE, SUBACK, UNSUBSCRIBE,
                                        //                      UNSUBACK, DISCONNECT, AUTH
    // USER_PROPERTY(38) -- is allowed to appear multiple to represent multiple name, value pairs.
    MAXIMUN_PACKET_SIZE(39),            // Four Byte Int        CONNECT, CONNACK
    WILDCARD_SUBSCRIPTION_AVAILABLE(40),// Byte                 CONNACK
    SUBSCRIPTION_IDENTIFIER_AVAILABLE(41),  // Byte             CONNACK
    SHARED_SUBSCRIPTION_AVAILABLE(42);  // Byte                 CONNACK
    ;

    private int value;
    private static final MqttPropertyType[] VALUES = MqttPropertyType.values();

    MqttPropertyType(int value) { this.value = value; }

    public int getValue() { return this.value; }

    public static MqttPropertyType valueOf(int value) {

        MqttPropertyType type = null;
        try {
            type = VALUES[value];
        } catch (ArrayIndexOutOfBoundsException ex) {

        }

        if (type == null) {
            throw new IllegalArgumentException("unknown property type: " + type);
        } else {
            return type;
        }
    }
}
