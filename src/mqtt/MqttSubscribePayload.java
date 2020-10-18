package mqtt;

/**
 * @author blake
 * date 2020-10-18 15:49:39
 **/

public class MqttSubscribePayload {

    private String topicFilter;
    private MqttQoS mqttQoS;
    private boolean isNoLocal;
    private boolean isRetainAsPublished;
    private int retainhandlingOption;
    private final int Reserved = 0;

    // Constructor
    public MqttSubscribePayload(String topicFilter, MqttQoS qos, boolean isNoLocal, boolean isRetainAsPublished, int retainhandlingOption) {
        this.topicFilter = topicFilter;
        this.mqttQoS = qos;
        this.isNoLocal = isNoLocal;
        this.isRetainAsPublished = isRetainAsPublished;
        this.retainhandlingOption = retainhandlingOption;
    }

    // Get Methord
    public String getTopicFilter() { return topicFilter; }

    public MqttQoS getMqttQoS() { return mqttQoS; }

    public boolean isNoLocal() { return isNoLocal; }

    public boolean isRetainAsPublished() { return isRetainAsPublished; }

    public int getRetainhandlingOption() { return retainhandlingOption; }
    
}
