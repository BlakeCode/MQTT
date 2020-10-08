package mqtt;

public class MqttConnAckPacket extends MqttPacket {

    public MqttConnAckPacket(MqttFixedHeader mqttFixedHeader, MqttConnAckVariableHeader variableHeader) {
        super(mqttFixedHeader, variableHeader);
    }

    public MqttFixedHeader getConnAckFixedHeader() { return super.getMqttFixedHeader(); }

    public MqttConnAckVariableHeader getConnAckVariableHeader() { return (MqttConnAckVariableHeader)super.getVariableHeader(); }
}
