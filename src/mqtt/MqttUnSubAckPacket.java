package mqtt;

public class MqttUnSubAckPacket extends MqttPacket {

    public MqttUnSubAckPacket(MqttFixedHeader fixedHeader, MqttSubAckVariableHeader variableHeader, MqttSubAckPayload payload) {
        super(fixedHeader, variableHeader, payload);
    }

    public MqttFixedHeader getUnSubAckFixedHeader() { return super.getMqttFixedHeader(); }

    public MqttSubAckVariableHeader getUnSubAckVariableHeader() { return (MqttSubAckVariableHeader)super.getVariableHeader(); }

    public MqttSubAckPayload getUnSubAckPayload() { return (MqttSubAckPayload)super.getPayload(); }
    
}
