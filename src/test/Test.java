package test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import mqtt.MqttPacketType;
import mqtt.MqttPublishReasonCode;

import java.io.ByteArrayOutputStream;

public class Test {

    public static void main(String[] args) {
        MqttPublishReasonCode reasonCode = MqttPublishReasonCode.SUCCESS;
        System.out.println(MqttPublishReasonCode.valueOf((byte)0x10));

    }

    private static void writeVariableLengthInt(ByteBuf buf, int num) {
        do {
            int digit = num % 128;
            num /= 128;
            if (num > 0) {
                digit |= 128;
            }
            buf.writeByte(digit);
        } while(num > 0);
    }

    public static byte[] encodeIntToByte(int number) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        do {
            int digit = number % 128;
            number /= 128;
            if (number > 0) {
                digit |= 128;
            }

            bos.write(digit);
        } while(number > 0);

        return bos.toByteArray();
    }

}
