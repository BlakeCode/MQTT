package mqtt;

/**
 * @author blake
 * date 2020-10-02 10:25:52
 **/

public class MqttConnectVariableHeader {

    private String protocalName;
    private int version;
    private boolean hasUserName;
    private boolean hasPassword;
    private boolean isWillRerain;
    private MqttQoS mqttQoS;
    private boolean isWillFlag;
    private boolean isCleanStat;
    private final int Reserved = 0;
    private int keepAliveSeconds;
    private MqttProperties connectProerties;

    public MqttConnectVariableHeader(String protocalName, int version, boolean hasUserName, boolean hasPassword, boolean isWillRerain,
             MqttQoS mqttQoS, boolean isWillFlag, boolean isCleanStat, int keepAliveSeconds, MqttProperties connectProerties) {
        this.protocalName = protocalName;
        this.version = version;
        this.hasUserName = hasUserName;
        this.hasPassword = hasPassword;
        this.isWillRerain = isWillRerain;
        this.mqttQoS = mqttQoS;
        this.isWillFlag = isWillFlag;
        this.isCleanStat = isCleanStat;
        this.keepAliveSeconds = keepAliveSeconds;
        this.connectProerties = connectProerties;
    }

    public String getProtocalName() { return protocalName; }

    public int getVersion() { return version; }

    public boolean isHasUserName() { return hasUserName; }

    public boolean isHasPassword() { return hasPassword; }

    public boolean isWillRerain() { return isWillRerain; }

    public MqttQoS getMqttQoS() { return mqttQoS; }

    public boolean isWillFlag() { return isWillFlag; }

    public boolean isCleanStat() { return isCleanStat; }

    public int getReserved() { return Reserved; }

    public int getKeepAliveSeconds() { return keepAliveSeconds; }

    public MqttProperties getConnectProerties() { return connectProerties; }
}
