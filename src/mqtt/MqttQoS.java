package mqtt;

/**
 * @author blake
 * date 2020-10-01 15:04:23
 **/

public enum MqttQoS {

    AT_MOST_ONCE(0),
    AT_LEAST_ONCE(1),
    EXACTLY_ONCE(2);

    private final int value;

    MqttQoS(int value) { this.value = value; }

    public int getValue() { return this.value; }
    
    /*
     * description:
     * @author blake
     * date   2020-10-01 15:30:25
     * @param value
     * @return mqtt.MqttQoS
     **/
    public static MqttQoS valueOf(int value) {
        switch (value) {
            case 0:
                return AT_MOST_ONCE;
            case 1:
                return AT_LEAST_ONCE;
            case 2:
                return EXACTLY_ONCE;
            default:
                throw new IllegalArgumentException("Illegal QoS: " + value);
        }
    }
}
