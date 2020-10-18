package mqtt;

/**
 * @author blake
 * date 2020-10-02 11:04:47
 **/

public class MqttConnectPacket extends MqttPacket{

    public MqttConnectPacket(MqttFixedHeader mqttFixedHeader, MqttConnectVariableHeader connectVariableHeader, MqttConnectPayload connectPayload) {
        super(mqttFixedHeader, connectVariableHeader, connectPayload);
    }

    public MqttFixedHeader getConnectFixedHeader() { return super.getMqttFixedHeader(); }

    public MqttConnectVariableHeader getConnectVariableHeader() { return (MqttConnectVariableHeader)super.getVariableHeader(); }

    public MqttConnectPayload getConnectPayload() { return (MqttConnectPayload)super.getPayload(); }

}
