package mqtt;

public class MqttPubAckPacket extends MqttPacket {

    public MqttPubAckPacket(MqttFixedHeader mqttFixedHeader, MqttPubAckVariableHeader variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }

    public MqttFixedHeader getPubAckFixedHeader() { return super.getMqttFixedHeader(); }

    public MqttPubAckVariableHeader getPubAckVariableHeader() { return (MqttPubAckVariableHeader)super.getVariableHeader(); }


}
