package mqtt;

public class MqttSubAckVariableHeader {

    private MqttProperties properties;

    public MqttSubAckVariableHeader(MqttProperties mqttProperties) {
        this.properties = mqttProperties;
    }

    public MqttProperties getProperties() { return properties; }

}
