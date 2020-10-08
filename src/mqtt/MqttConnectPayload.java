package mqtt;

/**
 * author blake
 * date 2020-10-02 10:47:43
 **/

public class MqttConnectPayload {

    private String clientIdentifier;
    private MqttProperties willProperties;
    private String willTopic;
    private byte[] willPayload;
    private String userName;
    private byte[] password;

    // Contructor : Will Flag == 1
    public MqttConnectPayload(String clientIdentifier, MqttProperties willProperties, String willTopic, byte[] willPayload, String userName, byte[] password) {
        this.clientIdentifier = clientIdentifier;
        this.willProperties = willProperties;
        this.willTopic = willTopic;
        this.willPayload = willPayload;
        this.userName = userName;
        this.password = password;
    }

    // Contructor : Will Flag == 0
    public MqttConnectPayload(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
    }

    // Get Methord
    public String getClientIdentifier() { return clientIdentifier; }

    public MqttProperties getWillProperties() { return willProperties; }

    public String getWillTopic() { return willTopic; }

    public byte[] getWillPayload() { return willPayload; }

    public String getUserName() { return userName; }

    public byte[] getPassword() { return password; }
}