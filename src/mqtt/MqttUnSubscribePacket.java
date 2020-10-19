package mqtt;

/**
 * @author blake
 * date 2020-10-19 16:39:38 
 **/

public class MqttUnSubscribePacket extends MqttPacket {

    public MqttUnSubscribePacket(MqttFixedHeader fixedHeader, MqttSubscribeVariableHeader variableHeader, MqttSubscribePayload payload) {
        super(fixedHeader, variableHeader, payload);
    }

    public MqttFixedHeader getUnSubscribeFixedHeader() { return super.getMqttFixedHeader(); }

    public MqttSubscribeVariableHeader getUnSubscribeVariableHeader() { return (MqttSubscribeVariableHeader)super.getVariableHeader(); }

    public MqttSubscribePayload getUnSubscribePayload() { return (MqttSubscribePayload)super.getPayload(); }
    
}
