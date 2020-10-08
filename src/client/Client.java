package client;

import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import mqtt.*;

import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {

        // port
        int port = 8088;
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Socket socket = new Socket("localhost", port);

        OutputStream outputStream = socket.getOutputStream();

        // 发送消息
        MqttFixedHeader connectFixedHeader =
                new MqttFixedHeader(MqttPacketType.CONNECT, 0);
        MqttConnectVariableHeader connectVariableHeader =
                new MqttConnectVariableHeader(MqttVersion.MQTT_5.getName(), MqttVersion.MQTT_5.getValue(), true, true,
                        false, MqttQoS.AT_MOST_ONCE, false, false, 20, null);
        MqttConnectPayload connectPayload =
                new MqttConnectPayload("0001", null, null, null, "root", "123456".getBytes());
        MqttConnectPacket connectPacket = new MqttConnectPacket(connectFixedHeader, connectVariableHeader, connectPayload);

        //socket.getOutputStream().write(connectPacket);


        outputStream.close();
        socket.close();
    }
}
