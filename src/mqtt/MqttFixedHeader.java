package mqtt;

/**
 * @author blake
 * date 2020-10-01 14:52:55
 **/

public class MqttFixedHeader{

    private MqttPacketType mqttPacketType;

    // PUBLISH -> DUP QoS RETAIN.
    private boolean isDUP;
    private MqttQoS mqttQoS;
    private boolean isReain;

    private int remainingLength;

    public MqttFixedHeader(MqttPacketType mqttPacketType, int remainingLength) {
        this(mqttPacketType, false, MqttQoS.AT_MOST_ONCE, false, remainingLength);
    }

    public MqttFixedHeader(MqttPacketType mqttPacketType, boolean isDUP, MqttQoS mqttQoS, boolean isReain, int remainingLength) {
        this.mqttPacketType = mqttPacketType;
        this.isDUP = isDUP;
        this.mqttQoS = mqttQoS;
        this.isReain = isReain;
        this.remainingLength = remainingLength;
    }

    public MqttPacketType getMqttPacketType() { return mqttPacketType; }

    public boolean isDUP() { return isDUP; }

    public MqttQoS getMqttQoS() { return mqttQoS; }

    public boolean isReain() { return isReain; }

    public int getRemainingLength() { return remainingLength; }
    public void setRemainingLength(int length) { this.remainingLength = length; }
}
