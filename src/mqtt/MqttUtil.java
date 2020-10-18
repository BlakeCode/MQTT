package mqtt;

import io.netty.util.CharsetUtil;

import java.io.ByteArrayOutputStream;

/**
 * @author blake
 * date 2020-10-07 20:36:52
 **/

public final class MqttUtil {

    /**
     * description: 1.5.2 encode short Int(0~65535) to Two Byte Int
     * @author blake
     * date 2020-10-07 10:04:00
     * @param number
     * @return byte[]
     **/
    public static byte[] encodeShortToTwoByte(int number) {

        if(number < 0 || number > 65535) {
            throw new IllegalArgumentException("Illegal Argument out of 0~65535");
        } else {

            byte[] result = new byte[2];

            result[0] = (byte)(number / 128);   // Most Significant Byte
            result[1] = (byte)(number % 128);   // Least Significant Byte

            return result;
        }
    }

    public static int decodeTwoByteToInt(byte[] bytes) {

        if(bytes.length != 2) {
            throw new IllegalArgumentException("Illegal argument: " + bytes);
        }

        return bytes[0] * 128 + bytes[1];
    }

    /**
     * description: 1.5.3 encode Int(0~2^32) to Four Byte Int
     * @author blake
     * date 2020-10-07 18:08:35
     * @param number
     * @return byte[]
     **/
    public static byte[] encodeIntToFourByte(int number) {

        if(number < 0) {
            throw new IllegalArgumentException("Illegal Argument: number");
        } else {

            byte[] result = new byte[4];

            for(int i = 3; i >= 0; i--) {
                result[i] = (byte)(number % 128);
                number /= 128;
            }
            return result;
        }
    }

    public static int decodeFourByteToInt(byte[] buffer) {

        if(buffer.length != 4) {
            throw new IllegalArgumentException("Illegal Argument: buffer");
        } else {
            int number = 0;
            for(int i = 0; i < 4; i++) {
                number += buffer[i];
                number *= 128;
            }
            return number;
        }
    }

    /**
     * description: 1.5.4 encode String to UTF-8 String
     * @author blake
     * date 2020-10-07 19:44:21
     * @param str
     * @return byte[]
     **/
    public static byte[] encodeStringToUTF8String(String str) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            byte[] result = str.getBytes("UTF-8");
            // preFixed 2 Byte header
            bos.write(encodeShortToTwoByte(result.length));
            bos.write(result);
        } catch (Exception e) {
            System.out.println("getBytes error: " + e);
        }

        return bos.toByteArray();
    }

    public static String decodeUTF8StringToString(byte[] strBytes) throws Exception{

        int length = decodeTwoByteToInt(getBytes(strBytes, 0, 2));
        if (length + 2 != strBytes.length) {
            throw new IllegalArgumentException("error in decodeUTF8StringToString().");
        }

        return new String(getBytes(strBytes, 2, strBytes.length), CharsetUtil.UTF_8);

    }

    /**
     * description: 1.5.5 encode a non-negative Int into Variable Bytes
     * @author blake
     * date 2020-10-02 21:13:12
     * @param number
     * @return byte[]
     **/
    public static byte[] encodeIntToVariableBytes(int number) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        do {
            int digit = number % 128;
            number /= 128;
            if (number > 0) {
                digit |= 128;
            }

            bos.write(digit);
        } while (number > 0);

        return bos.toByteArray();
    }

    public static byte[] getBytes(byte[] buffer, int start, int end) {
        byte[] result = new byte[end - start];
        int index = 0;
        for(int i = start; i < end; i++) {
            result[index++] = buffer[i];
        }
        return result;
    }
}
