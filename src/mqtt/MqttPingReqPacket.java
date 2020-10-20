package mqtt;

/**
 * @author blake
 * date 2020-10-20 10:39:54 
 **/

public class MqttPingReqPacket extends MqttPacket {

    public MqttPingReqPacket(MqttFixedHeader fixedHeader) {
        super(fixedHeader, null, null);
    }

    public MqttFixedHeader getPingReqFixedHeader() { return super.getMqttFixedHeader(); }

}
