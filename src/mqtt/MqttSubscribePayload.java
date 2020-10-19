package mqtt;

import java.util.ArrayList;

/**
 * @author blake
 * date 2020-10-18 15:49:39
 **/

public class MqttSubscribePayload {

    private ArrayList<MqttTopicSubscription> topicSubscriptionList = new ArrayList<>();

    public ArrayList<MqttTopicSubscription> getTopicSubscriptionList() { return topicSubscriptionList; }
}
