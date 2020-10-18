package mqtt;

public class MqttSubscribePacket extends MqttPacket {

    public MqttSubscribePacket(MqttFixedHeader fixedHeader, MqttSubscribeVariableHeader variableHeader, MqttSubscribePayload payload) {
        super(fixedHeader, variableHeader, payload);
    }

    public MqttFixedHeader getSubscribeFixedHeader() { return super.getMqttFixedHeader(); }

    public MqttSubscribeVariableHeader getSubscribeVariableHeader() { return (MqttSubscribeVariableHeader)super.getVariableHeader(); }

    public MqttSubscribePayload getSubscribePayload() { return (MqttSubscribePayload)super.getPayload(); }

}
