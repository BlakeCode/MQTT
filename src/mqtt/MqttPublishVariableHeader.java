package mqtt;

/**
 * @author blake
 * date 2020-10-17 16:00:48
 **/

public class MqttPublishVariableHeader {

    private String topicName;
    private int packetIdentifier;
    private MqttProperties publishProperties;

    public MqttPublishVariableHeader(String topicName, int packetIdentifier) {
        this(topicName, packetIdentifier, null);
    }

    public MqttPublishVariableHeader(String topicName, int packetIdentifier, MqttProperties publishProperties) {
        this.topicName = topicName;
        this.packetIdentifier = packetIdentifier;
        this.publishProperties = publishProperties;
    }

    public String getTopicName() { return topicName; }

    public int getPacketIdentifier() { return packetIdentifier; }

    public MqttProperties getPublishProperties() { return publishProperties; }

    @Override
    public String toString() {
        return "MqttPublishVariableHeader{" +
                "topicName='" + topicName + '\'' +
                ", packetIdentifier=" + packetIdentifier +
                '}';
    }

}
