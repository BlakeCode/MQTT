package mqtt;

/**
 * @author blake
 * date 2020-10-19 16:39:38 
 **/

public class MqttUnsubscribePacket extends MqttPacket {

    public MqttUnsubscribePacket(MqttFixedHeader fixedHeader, MqttSubscribeVariableHeader variableHeader, MqttUnsubscribePayload payload) {
        super(fixedHeader, variableHeader, payload);
    }

    public MqttFixedHeader getUnsubscribeFixedHeader() { return super.getMqttFixedHeader(); }

    public MqttSubscribeVariableHeader getUnsubscribeVariableHeader() { return (MqttSubscribeVariableHeader)super.getVariableHeader(); }

    public MqttSubscribePayload getUnsubscribePayload() { return (MqttSubscribePayload)super.getPayload(); }
    
}
