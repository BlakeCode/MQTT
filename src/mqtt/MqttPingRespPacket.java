package mqtt;

/**
 * @author blake
 * date 2020-10-20 10:49:39 
 **/

public class MqttPingRespPacket extends MqttPacket {

    public MqttPingRespPacket(MqttFixedHeader fixedHeader) {
        super(fixedHeader, null, null);
    }

    public MqttFixedHeader getPingRespFixedHeader() { return super.getMqttFixedHeader(); }

}
