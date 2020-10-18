package mqtt;

/**
 * @author blake
 * date 2020-10-01 14:49:17
 **/

public class MqttPacket {

    public MqttFixedHeader mqttFixedHeader;
    public Object variableHeader;
    public Object payload;

    public MqttPacket(MqttFixedHeader mqttFixedHeader) {
        this(mqttFixedHeader, (Object)null, (Object)null);
    }

    public MqttPacket(MqttFixedHeader mqttFixedHeader, Object variableHeader) {
        this(mqttFixedHeader, variableHeader, (Object)null);
    }

    public MqttPacket(MqttFixedHeader mqttFixedHeader, Object variableHeader, Object payload) {
        this.mqttFixedHeader = mqttFixedHeader;
        this.variableHeader = variableHeader;
        this.payload = payload;
    }

    public MqttFixedHeader getMqttFixedHeader() { return mqttFixedHeader; }

    public Object getVariableHeader() { return variableHeader; }

    public Object getPayload() { return payload; }

    @Override
    public String toString() {
        return "MqttPacket{" +
                "mqttFixedHeader=" + (this.getMqttFixedHeader() != null ? this.getMqttFixedHeader().toString() : "") +
                ", variableHeader=" + (this.getVariableHeader() != null ? this.getVariableHeader().toString() : "") +
                ", payload=" + (this.getPayload() != null ? this.getPayload().toString() : "") +
                '}';
    }
}
