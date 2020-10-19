package mqtt;

public class MqttSubAckVariableHeader {

    private int packetIdentifier;
    private MqttProperties properties;

    public MqttSubAckVariableHeader(int packetIdentifier, MqttProperties mqttProperties) {
        this.packetIdentifier = packetIdentifier;
        this.properties = mqttProperties;
    }

    public MqttProperties getProperties() { return properties; }
    public int getPacketIdentifier() { return packetIdentifier;}

}
