package mqtt;

/**
 * @author blake
 * date 2020-10-17 16:00:43 
 **/

public class MqttPublishPacket extends MqttPacket {

    public MqttPublishPacket(MqttFixedHeader mqttFixedHeader, MqttPublishVariableHeader variableHeader, byte[] payload) {
        super(mqttFixedHeader, variableHeader, payload);
    }

    public MqttFixedHeader getPublishFixedHeader() { return super.getMqttFixedHeader(); }

    public MqttPublishVariableHeader getPublishVariableHeader() { return (MqttPublishVariableHeader)super.getVariableHeader(); }

    public byte[] getPublishPayload() { return (byte[])super.payload; }

    // public String getPublishMessage() throws Exception { return MqttUtil.decodeUTF8StringToString(this.getPublishPayload()); }

}
