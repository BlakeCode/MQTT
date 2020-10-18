package mqtt;

public class MqttSubscribeVariableHeader {

    private int packetIdentifier;
    private MqttProperties properties;

    public MqttSubscribeVariableHeader(int packetidentifier) {
        this(packetidentifier, null);
    }

    public MqttSubscribeVariableHeader(int packetIdentifier, MqttProperties properties) {
        this.packetIdentifier = packetIdentifier;
        this.properties = properties;
    }

    public int getPacketIdentifier() { return packetIdentifier; }

    public MqttProperties getProperties() { return properties; }

    @Override
    public String toString() {
        return "MqttSubscribeVariableHeader{" +
                "packetIdentifier=" + packetIdentifier +
                ", properties=" + properties +
                '}';
    }

}
