package mqtt;

/**
 * author blake
 * date 2020-10-02 14:34:07
 **/

public class MqttConnAckVariableHeader {

    private boolean isSessionPresent;
    private MqttConnectReasonCode connectReasonCode;
    private MqttProperties connackProperties;

    public MqttConnAckVariableHeader(boolean isSessionPresent, MqttConnectReasonCode connectReasonCode, MqttProperties connackProperties) {
        this.isSessionPresent = isSessionPresent;
        this.connectReasonCode = connectReasonCode;
        this.connackProperties = connackProperties;
    }

    public boolean isSessionPresent() {
        return isSessionPresent;
    }

    public MqttConnectReasonCode getConnectReasonCode() {
        return connectReasonCode;
    }

    public MqttProperties getConnackProperties() {
        return connackProperties;
    }
}
