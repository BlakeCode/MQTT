package mqtt;

/**
 * @author blake
 * date 2020-10-18 20:37:31
 **/

public class MqttSubAckPacket extends MqttPacket {

    public MqttSubAckPacket(MqttFixedHeader fixedHeader, MqttSubAckVariableHeader variableHeader, MqttSubAckPayload payload) {
        super(fixedHeader, variableHeader, payload);
    }

    public MqttFixedHeader getSubAckFixedHeader() { return super.getMqttFixedHeader(); }

    public MqttSubAckVariableHeader getSubAckVariableHeader() { return (MqttSubAckVariableHeader)super.getVariableHeader(); }

    public MqttSubAckPayload getSubAckPayload() { return (MqttSubAckPayload)super.getPayload(); }
    
}
