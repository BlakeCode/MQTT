package mqtt;

public class MqttSubscribeVariableHeader {

    private int packetidentifier;
    private MqttProperties properties;

    public MqttSubscribeVariableHeader(int packetidentifier) {
        this(packetidentifier, null);
    }

    public MqttSubscribeVariableHeader(int packetidentifier, MqttProperties properties) {
        this.packetidentifier = packetidentifier;
        this.properties = properties;
    }

    public int getPacketidentifier() { return packetidentifier; }

    public MqttProperties getProperties() { return properties; }

    @Override
    public String toString() {
        return "MqttSubscribeVariableHeader{" +
                "packetidentifier=" + packetidentifier +
                ", properties=" + properties +
                '}';
    }

}
