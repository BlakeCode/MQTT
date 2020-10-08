package mqtt;

/**
 * author blake
 * date 2020-10-02 12:36:44
 **/

public enum MqttVersion {

    MQTT_3_1_1("MQTT", (byte)4),
    MQTT_5("MQTT", (byte)5);

    private String name;
    private int value;

    MqttVersion(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }

    public int getValue() { return value; }

    public static MqttVersion valueOf(String name, int value) {
        MqttVersion mv;
        switch (value) {
            case 4:
                mv = MQTT_3_1_1;
                break;
            case 5:
                mv = MQTT_5;
                break;
            default:
                mv = null;
                break;
        }

        if(mv == null) {
            throw new IllegalArgumentException("illegal Version: " + value);
        } else if(!mv.name.equals(name)) {
            throw new IllegalArgumentException("illegal Name,Version pairs: " + name + " - " + value);
        }

        return mv;
    }
}
