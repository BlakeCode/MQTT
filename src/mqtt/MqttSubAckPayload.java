package mqtt;

import java.util.ArrayList;

/**
 * @author blake
 * date 2020-10-18 20:37:39
 **/

public class MqttSubAckPayload {

    private ArrayList<MqttSubscribeReasonCode> reasonCodeList = new ArrayList<>();

    public ArrayList<MqttSubscribeReasonCode> getReasonCodeList() { return reasonCodeList; }
}
