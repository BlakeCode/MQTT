package test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import mqtt.MqttPacketType;

import java.io.ByteArrayOutputStream;

public class Test {

    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(5);
        writeVariableLengthInt(buf, 16384);
        for(byte b : buf.array()) {
            System.out.printf("%x ", b);
        }
        boolean a = (0x88 & 0x80) == 128;
        System.out.println(a);

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
