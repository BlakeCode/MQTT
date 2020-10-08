package mqtt;

/**
 * author blake
 * date 2020-10-02 14:22:41
 **/

public class MqttPacketFactory {

    public static MqttPacket newPacket(MqttFixedHeader mqttFixedHeader, Object variableHeader, Object payload) {
        switch (mqttFixedHeader.getMqttPacketType()) {
            case CONNECT:
                return new MqttConnectPacket(mqttFixedHeader, (MqttConnectVariableHeader)variableHeader, (MqttConnectPayload)payload);
            case CONNACK:
                return new MqttConnAckPacket(mqttFixedHeader, (MqttConnAckVariableHeader)variableHeader);

            default:
                throw new IllegalArgumentException("Illegal message type: " + mqttFixedHeader.getMqttPacketType());
        }
    }
}
