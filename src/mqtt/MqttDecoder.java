package mqtt;

import io.netty.util.CharsetUtil;

import java.util.Arrays;

/**
 * @author blake
 * date 2020-10-07 20:39:30
 **/

public class MqttDecoder {

    private MqttFixedHeader fixedHeader;
    private Object variableHeader;
    private Object payload;

    private int totalLength, currentIndex;

    /**
     * description: decode the byte[] to MqttPacket
     * @author blake
     * date 2020-10-08 09:31:00
     * @param buffer
     * @return void
     **/
    public void decode(byte[] buffer) throws Exception {

        this.totalLength = buffer.length;
        fixedHeader = decodeFixedHeader(buffer);

        if (fixedHeader.getRemainingLength() == this.totalLength - currentIndex) {

            variableHeader = decodeVariableHeader(buffer);

            MqttPacket packet;
            if(currentIndex + 1 == totalLength) {
                packet = new MqttPacket(fixedHeader, variableHeader);
            } else if (currentIndex + 1 < totalLength) {
                payload = decodePayload(buffer);
                packet = new MqttPacket(fixedHeader, variableHeader, payload);
            } else {
                throw new Exception("decode payload error.");
            }
        } else {
            throw new Exception("decode Variable Header error.");
        }

    }

    /**
     * description: decode Fixed Header
     * @author blake
     * date 2020-10-08 10:09:09
     * @param buffer
     * @return mqtt.MqttFixedHeader
     **/
    private MqttFixedHeader decodeFixedHeader(byte[] buffer) throws Exception{

        // decode byte1 of Fixed Header
        int messageType = buffer[0] >> 4;                       // bit 7 - 4
        MqttPacketType packetType = MqttPacketType.valueOf(messageType);
        boolean isDUP = (buffer[0] & 8) == 8;                   // bit 3
        MqttQoS mqttQoS = MqttQoS.valueOf((buffer[0] & 6) >> 1);  // bit 2 - 1
        boolean isRetain = (buffer[0] & 1) != 0;                // bit 0

        // decode Remaining Length of Fixed Header
        int remainLength = 0;
        int multiplier = 1;
        int byteIndex = 1;
        do {
            remainLength += (buffer[byteIndex] & 0x7F) * multiplier;
            multiplier *= 128;
            byteIndex++;
        } while(byteIndex <= 4 && (buffer[byteIndex] & 0x80) != 0);

        // check
        this.currentIndex = byteIndex + 1;
        if (byteIndex > 4) {
            throw new Exception("remain length bigger than 4 bytes.");
        } else if (remainLength + byteIndex + 1 != this.totalLength) {
            throw new Exception("packet length error.");
        }

        if (packetType == MqttPacketType.PUBLISH) {
            return new MqttFixedHeader(packetType, isDUP, mqttQoS, isRetain, remainLength);
        } else {
            return new MqttFixedHeader(packetType, remainLength);
        }
    }

    /**
     * description: decode Variable Header
     * @author blake
     * date 2020-10-08 10:42:17
     * @param buffer
     * @return Object
     **/
    private Object decodeVariableHeader(byte[] buffer) throws Exception {

        MqttPacketType packetType = fixedHeader.getMqttPacketType();

        switch (packetType) {
            case CONNECT:
                return decodeConnectVariableHeader(buffer);
            case CONNACK:
                return decodeConnAckVariableHeader(buffer);
            case PUBLISH:
                return decodePublishVariableHeader(buffer);
            case PUBACK:
            case PUBREC:
            case PUBREL:
            case PUBCOMP:
                return decodePubAckVariableHeader(buffer);
            default:
                return null;
        }
    }

    /**
     * description: decode Payload in some packet type
     * @author blake
     * date   2020-10-17 15:46:30
     * @param buffer
     * @return Object
     **/
    private Object decodePayload(byte[] buffer) throws Exception {

        return null;
    }

    /**
     * description: decode Variable Header - Connect
     * @author blake
     * date 2020-10-08 10:45:42
     * @param buffer
     * @return mqtt.MqttConnectVariableHeader
     **/
    private MqttConnectVariableHeader decodeConnectVariableHeader(byte[] buffer) throws Exception {

        // byte 1 - 6
        byte[] protocalName = new byte[6];
        System.arraycopy(buffer,currentIndex, protocalName,0,6);
        currentIndex += 6;
        if(!Arrays.equals(protocalName, new byte[]{0x00, 0x04, 'M', 'Q', 'T', 'T'})) {
            throw new Exception("Illegal Protocal Name");
        }

        // byte 7
        byte protocalVersion = buffer[currentIndex++];
        MqttVersion version = MqttVersion.valueOf("MQTT", protocalVersion);

        // byte 8
        byte connectFlags = buffer[currentIndex++];
        boolean hasUserName = (connectFlags & 0x80) == 0x80;
        boolean hasPassword = (connectFlags & 0x40) == 0x40;
        boolean isWillRerain = (connectFlags & 0x20) == 0x20;
        MqttQoS mqttQoS = MqttQoS.valueOf((connectFlags & 0x18) >> 3);
        boolean isWillFlag = (connectFlags & 0x04) == 0x04;
        boolean isCleanStat = (connectFlags & 0x02) == 0x02;
        int Reserved = connectFlags & 0x01;

        // byte 9 - 10
        int keepAliveSeconds = MqttUtil.decodeTwoByteToInt(MqttUtil.getBytes(buffer, currentIndex, currentIndex + 2));
        currentIndex += 2;

        // decode Connect Properties
        MqttProperties connectProerties = decodeProperties(buffer);

        return new MqttConnectVariableHeader(version.getName(), version.getValue(), hasUserName, hasPassword, isWillRerain,
                mqttQoS, isWillFlag, isCleanStat, keepAliveSeconds, connectProerties);
    }

    /**
     * description: decode Variable Header - ConnAck
     * @author blake
     * date 2020-10-08 10:46:41
     * @param buffer
     * @return mqtt.MqttConnAckVariableHeader
     **/
    private MqttConnAckVariableHeader decodeConnAckVariableHeader(byte[] buffer) throws Exception{

        // decode Session Present & Connect Reason Code
        boolean isSessionPresent = (buffer[currentIndex++] & 0x01) == 0x01;
        MqttConnectReasonCode reasonCode = MqttConnectReasonCode.valueOf(buffer[currentIndex++]);

        // decode ConnAck Properties
        MqttProperties connAckProerties = decodeProperties(buffer);

        return new MqttConnAckVariableHeader(isSessionPresent, reasonCode, connAckProerties);
    }

    /**
     * description: decode Variable Header - Publish
     * @author blake
     * date   2020-10-17 22:17:42
     * @param buffer
     * @return mqtt.MqttPublishVariableHeader
     **/
    private MqttPublishVariableHeader decodePublishVariableHeader(byte[] buffer) throws Exception {

        // decode Topic Name
        int topicNameLength = MqttUtil.decodeTwoByteToInt(MqttUtil.getBytes(buffer, currentIndex, currentIndex + 2));
        String topicName = MqttUtil.decodeUTF8StringToString(MqttUtil.getBytes(buffer, currentIndex, currentIndex + topicNameLength));
        currentIndex += topicNameLength;

        // decode Packet Identifier - QoS is 1 or 2
        int packetIdentifier = 0;
        if (fixedHeader.getMqttQoS().getValue() > 0) {
            packetIdentifier = MqttUtil.decodeTwoByteToInt(MqttUtil.getBytes(buffer, currentIndex, currentIndex + 2));
            currentIndex += 2;
        }

        // decode Publish Properties
        MqttProperties publishProperties = decodeProperties(buffer);

        // decode Publish Payload
        byte[] payload = MqttUtil.getBytes(buffer, currentIndex, topicNameLength);

        return new MqttPublishVariableHeader(topicName, packetIdentifier, publishProperties);

    }

    /**
     * description: decode Variable Header - PubAck
     * @author blake
     * date   2020-10-17 22:19:53
     * @param buffer
     * @return mqtt.MqttPubAckVariableHeader
     **/
    private MqttPubAckVariableHeader decodePubAckVariableHeader(byte[] buffer) throws Exception {

        // decode PacketIdentifier
        int packetIdentifier = MqttUtil.decodeTwoByteToInt(MqttUtil.getBytes(buffer, currentIndex, currentIndex + 2));
        currentIndex += 2;

        // decode PublishReasonCode
        MqttPublishReasonCode reasonCode = MqttPublishReasonCode.SUCCESS;
        if (fixedHeader.getRemainingLength() > 2) {
            MqttPublishReasonCode.valueOf(buffer[currentIndex]);
        }

        // decode Property Length
        int propertiesLength = 0;
        if (fixedHeader.getRemainingLength() < 4) {
            propertiesLength = 0;
            return new MqttPubAckVariableHeader(packetIdentifier, reasonCode);
        } else {
            MqttProperties properties = decodeProperties(buffer);
            return new MqttPubAckVariableHeader(packetIdentifier, reasonCode, properties);
        }
    }

    /**
     * description: decode Properties
     * @author blake
     * date   2020-10-08 14:16:13
     * @param buffer
     * @return mqtt.MqttProperties
     **/
    private MqttProperties decodeProperties(byte[] buffer) throws Exception {

        if(buffer[currentIndex] == 0) {
            return null;
        }

        // decode Properties Length of Variable Header
        int propertiesLength = 0;
        int multiplier = 1;
        int byteIndex = 0;
        do {
            propertiesLength += (buffer[currentIndex + byteIndex] & 0x7F) * multiplier;
            multiplier *= 128;
            byteIndex++;
        } while(byteIndex <= 4 && (buffer[currentIndex + byteIndex] & 0x80) != 0);

        if (byteIndex > 4) {
            throw new Exception("remain length bigger than 4 bytes.");
        }

        currentIndex += byteIndex;

        // decode Properties
        MqttProperties properties = new MqttProperties();
        for(int i = currentIndex; i < currentIndex + propertiesLength; i++) {
            switch(MqttPropertyType.valueOf(buffer[i])) {

                // Byte
                case PAYLOAD_FORMAT_INDICATOR:
                case REQUESR_PROBLEM_INFORMATION:
                case REQUEST_RESPONSE_INFORMATION:
                case MAXIMUM_QOS:
                case RETAIN_AVAILABLE:
                case WILDCARD_SUBSCRIPTION_AVAILABLE:
                case SUBSCRIPTION_IDENTIFIER_AVAILABLE:
                case SHARED_SUBSCRIPTION_AVAILABLE:
                    properties.add(properties.new MqttProperty<>(buffer[i+1], buffer[i]));
                    i += 2;
                    break;

                // Two Byte Integer
                case RECEIVE_MAXIMUM:
                case CORRELATION_DATA:
                case SERVER_KEEP_ALIVE:
                case AUTHENTICATION_DATA:
                case TOPIC_ALIAS_MAXIMUM:
                case TOPIC_ALIAS:
                    properties.add(properties.new MqttProperty<>(
                            MqttUtil.decodeTwoByteToInt(MqttUtil.getBytes(buffer, i+1,i+3)), buffer[i]
                    ));
                    i += 3;
                    break;

                // Four Byte Integer
                case MESSAGE_EXPIRY_INTERVAL:
                case SESSION_EXPIRY_INTERVAL:
                case WILL_DELAY_INTERVAL:
                case MAXIMUN_PACKET_SIZE:
                    properties.add(properties.new MqttProperty<>(
                        MqttUtil.decodeFourByteToInt(MqttUtil.getBytes(buffer, i+1, i+5)), buffer[i]
                    ));
                    i += 5;
                    break;

                // UTF-8 Encoded String
                case CONTENT_TYPE:
                case RESPONSE_TOPIC:
                case ASSIGNED_CLIENT_IDENTIFIER:
                case AUTHENTICATION_METHORD:
                case RESPONSE_INFORMATION:
                case SERVER_REFERENCE:
                case REASON_STRING:
                    int strLength = buffer[i+1] * 128 + buffer[i+2];
                    String str = new String(MqttUtil.getBytes(buffer,i+3, i+3 + strLength), CharsetUtil.UTF_8);
                    properties.add(properties.new MqttProperty<>(str, buffer[i]));
                    i += 3 + strLength;
                    break;

                // Variable Byte Integer
                case SUBSCRIPTION_IDENTIFIER:

                    int value = 0;
                    int multi = 1;
                    int index = 1;
                    do {
                        value += (buffer[i + index] & 0x7F) * multi;
                        multi *= 128;
                        index++;
                    } while(index <= 4 && (buffer[i + index] & 0x80) != 0);

                    if (index > 4) {
                        throw new Exception("remain length bigger than 4 bytes.");
                    }
                    properties.add(properties.new MqttProperty<>(value, buffer[i]));
                    i += 1 + index;
                    break;

                // UTF-8 String Pair
                case USER_PROPERTY:
                    break;

                default:
                    throw new Exception("Unknown property type: ");
            }
        }
        currentIndex += propertiesLength;
        properties.setPropertiesByteLength(propertiesLength);
        return properties;
    }

}
