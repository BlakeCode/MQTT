package mqtt;

public class MqttPubAckVariableHeader {

    private int packetIdentifier;
    private MqttPubAckReasonCode reasonCode;
    private int propertyLength;
    private MqttProperties properties;

    public MqttPubAckVariableHeader(int packetIdentifier, MqttPubAckReasonCode reasonCode) {
        this.packetIdentifier = packetIdentifier;
        this.reasonCode = reasonCode;
        this.properties = null;
        this.propertyLength = 0;
    }

    public MqttPubAckVariableHeader(int packetIdentifier, MqttPubAckReasonCode reasonCode, MqttProperties properties) {
        this.packetIdentifier = packetIdentifier;
        this.reasonCode = reasonCode;
        this.properties = properties;
    }

    public int getPacketIdentifier() { return packetIdentifier; }

    public MqttPubAckReasonCode getReasonCode() { return reasonCode; }

    public int getPropertyLength() { return propertyLength; }

    public MqttProperties getProperties() { return properties; }

    public void setPropertyLength(int propertyLength) { this.propertyLength = propertyLength; }
}
