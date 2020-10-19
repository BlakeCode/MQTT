package mqtt;

public class MqttTopicSubscription {

    private String topicFilter;
    private MqttQoS mqttQoS;
    private boolean isNoLocal;
    private boolean isRetainAsPublished;
    private int retainhandlingOption;
    private final int Reserved = 0;

    public MqttTopicSubscription(String topicFilter, MqttQoS mqttQoS, boolean isNoLocal, boolean isRetainAsPublished, int retainhandlingOption) {
        this.topicFilter = topicFilter;
        this.mqttQoS = mqttQoS;
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
