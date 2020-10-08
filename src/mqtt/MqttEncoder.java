package mqtt;

import java.io.*;

/**
 * author blake
 * date 2020-10-02 17:59:27
 **/

public class MqttEncoder {

    public static byte[] encode(MqttPacket packet) throws Exception{
        switch (packet.getMqttFixedHeader().getMqttPacketType()) {
            case CONNECT:
                return encodeConnectPacket((MqttConnectPacket)packet);
            default:
                throw new IllegalArgumentException("Unknown packet type: " +
                        packet.getMqttFixedHeader().getMqttPacketType()
                );
        }
    }

    /**
     * description: encode CONNECT packet
     * author blake
     * date 2020-10-02 21:17:35
     * param: packet
     * return byte[]
     **/
    public static byte[] encodeConnectPacket(MqttConnectPacket packet) throws Exception {

        // Fixed + Variable + Payload
        MqttFixedHeader fixedHeader = packet.getConnectFixedHeader();
        MqttConnectVariableHeader variableHeader = packet.getConnectVariableHeader();
        MqttConnectPayload payload = packet.getConnectPayload();

        // 3.1.2 byte 1 - 7 of Variable Header
        byte[] protocalNameAndVersion = new byte[]{0x0, 0x4,
                'M', 'Q', 'T', 'T',
                (byte)variableHeader.getVersion()};

        // 3.1.2 byte 8 of Variable Header
        byte connectFlags = encodeConnectVariableHeaderFlags(variableHeader);

        // 3.1.2 byte 9 - 10 of Variable Header
        byte[] keepAliveBytes = MqttUtil.encodeShortToTwoByte(variableHeader.getKeepAliveSeconds());

        // 3.1.2.11 Connect Properties
        byte[] connectPropertyBytes = encodeProperties(variableHeader.getConnectProerties());
        byte[] connectPropertyBytesLength = MqttUtil.encodeIntToByte(connectPropertyBytes.length);
        int variableByteSize = 10 + connectPropertyBytes.length + connectPropertyBytesLength.length;

        // 3.1.3 Connect Payload
        int payloadByteSize = 0;
        ByteArrayOutputStream bosPayload = new ByteArrayOutputStream();

        // 3.1.3.1 Clinet IDentifier
        String clientIdentifier = payload.getClientIdentifier();
        byte[] clientIdentifierBytes = clientIdentifier.getBytes("UTF-8");
        payloadByteSize += 2 + clientIdentifierBytes.length;
        bosPayload.write(MqttUtil.encodeShortToTwoByte(clientIdentifierBytes.length));
        bosPayload.write(clientIdentifierBytes);

        if(variableHeader.isWillFlag()) {

            // 3.1.3.2 Will Properties
            byte[] willPropertyBytes = encodeProperties(payload.getWillProperties());
            byte[] willPropertyBytesLength = MqttUtil.encodeIntToByte(willPropertyBytes.length);
            payloadByteSize += willPropertyBytesLength.length + willPropertyBytes.length;
            bosPayload.write(willPropertyBytesLength);
            bosPayload.write(willPropertyBytes);

            // 3.1.3.3 Will Topic
            String willTopic = payload.getWillTopic();
            byte[] willTpicBytes = willTopic.getBytes("UTF-8");
            payloadByteSize += 2 + willTpicBytes.length;
            bosPayload.write(MqttUtil.encodeShortToTwoByte(willTpicBytes.length));
            bosPayload.write(willTpicBytes);

            // 3.1.3.4 Will Payload
            byte[] willPayload = payload.getWillPayload();
            payloadByteSize += 2 + willPayload.length;
            bosPayload.write(MqttUtil.encodeShortToTwoByte(willPayload.length));
            bosPayload.write(willPayload);
        }

        // 3.1.3.5 User Name
        if(variableHeader.isHasUserName()) {
            byte[] userName = payload.getUserName().getBytes("UTF-8");
            payloadByteSize += 2 + userName.length;
            bosPayload.write(MqttUtil.encodeShortToTwoByte(userName.length));
            bosPayload.write(userName);
        }

        // 3.1.3.6 Password
        if(variableHeader.isHasPassword()) {
            byte[] passwprd = payload.getPassword();
            payloadByteSize += 2 + passwprd.length;
            bosPayload.write(MqttUtil.encodeShortToTwoByte(passwprd.length));
            bosPayload.write(passwprd);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // write the 2nd byte of Fixed Header
        if(fixedHeader.getRemainingLength() != variableByteSize + payloadByteSize) {
            throw new Exception("Reamaining Length error");
        } else {
            bos.write(MqttUtil.encodeIntToByte(fixedHeader.getRemainingLength()));
        }

        // write Variable Header
        bos.write(protocalNameAndVersion);      // byte 1 - 7
        bos.write(connectFlags);                // byte 8
        bos.write(keepAliveBytes);              // byte 9 - 10
        bos.write(connectPropertyBytesLength);  // byte 11...
        bos.write(connectPropertyBytes);

        // write Payload
        bos.write(bosPayload.toByteArray());

        return bos.toByteArray();
    }

    public static byte[] encodeConnAckPacket(MqttConnAckPacket packet) throws Exception {

        // Fixed + Variable
        MqttFixedHeader fixedHeader = packet.getConnAckFixedHeader();
        MqttConnAckVariableHeader variableHeader = packet.getConnAckVariableHeader();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte sessionPresent = (byte)(variableHeader.isSessionPresent() ? 1 : 0);
        byte connectReasonCode = variableHeader.getConnectReasonCode().getValue();
        byte[] connackProperties = encodeProperties(variableHeader.getConnackProperties());
        byte[] connackPropertiesLength = MqttUtil.encodeIntToByte(connackProperties.length);

        // 3.2.1 write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // 3.2.1 write the 2nd byte of Fixed Header
        bos.write(MqttUtil.encodeIntToByte(2 + connackPropertiesLength.length +
                connackProperties.length));

        // 3.2.2 write Connack Variable Header
        bos.write(sessionPresent);
        bos.write(connectReasonCode);
        bos.write(connackPropertiesLength);
        bos.write(connackProperties);

        return bos.toByteArray();
    }
    /**
     * description: encode Properties to byte[]
     * author blake
     * date 2020-10-07 18:02:30
     * param: mqttProperties
     * return byte[]
     **/
    public static byte[] encodeProperties(MqttProperties mqttProperties) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            for(MqttProperties.MqttProperty mqttProperty : mqttProperties.getPropertyList()) {
                switch (MqttPropertyType.valueOf(mqttProperty.getPropertyId())) {

                    // Byte
                    case PAYLOAD_FORMAT_INDICATOR:
                    case REQUESR_PROBLEM_INFORMATION:
                    case REQUEST_RESPONSE_INFORMATION:
                    case MAXIMUM_QOS:
                    case RETAIN_AVAILABLE:
                    case WILDCARD_SUBSCRIPTION_AVAILABLE:
                    case SUBSCRIPTION_IDENTIFIER_AVAILABLE:
                    case SHARED_SUBSCRIPTION_AVAILABLE:
                        bos.write(mqttProperty.getPropertyId());
                        bos.write((byte) mqttProperty.getValue());
                        break;

                    // Two Byte Integer
                    case RECEIVE_MAXIMUM:
                    case CORRELATION_DATA:
                    case SERVER_KEEP_ALIVE:
                    case AUTHENTICATION_DATA:
                    case TOPIC_ALIAS_MAXIMUM:
                    case TOPIC_ALIAS:
                        bos.write(mqttProperty.getPropertyId());
                        bos.write(MqttUtil.encodeShortToTwoByte((Integer) mqttProperty.getValue()));
                        break;

                    // Four Byte Integer
                    case MESSAGE_EXPIRY_INTERVAL:
                    case SESSION_EXPIRY_INTERVAL:
                    case WILL_DELAY_INTERVAL:
                    case MAXIMUN_PACKET_SIZE:
                        bos.write(mqttProperty.getPropertyId());
                        bos.write(MqttUtil.encodeIntToFourByte((Integer) mqttProperty.getValue()));
                        break;

                    // UTF-8 Encoded String
                    case CONTENT_TYPE:
                    case RESPONSE_TOPIC:
                    case ASSIGNED_CLIENT_IDENTIFIER:
                    case AUTHENTICATION_METHORD:
                    case RESPONSE_INFORMATION:
                    case SERVER_REFERENCE:
                    case REASON_STRING:
                        bos.write(mqttProperty.getPropertyId());
                        String content = (String) mqttProperty.getValue();
                        byte[] contentBytes = content.getBytes("UTF-8");
                        bos.write(MqttUtil.encodeShortToTwoByte(contentBytes.length));
                        bos.write(contentBytes);
                        break;

                    // Variable Byte Integer
                    case SUBSCRIPTION_IDENTIFIER:
                        bos.write(mqttProperty.getPropertyId());
                        bos.write(MqttUtil.encodeIntToByte((Integer) mqttProperty.getValue()));
                        break;

                    // UTF-8 String Pair
                    case USER_PROPERTY:
                        break;

                    default:
                        throw new Exception("Unknown property type: ");
                }
            }
        } catch (Exception e) {
                System.out.println("IO exception : " + e);
        }

        return bos.toByteArray();
    }

    /**
     * description: 2.1.3 编码 Fixed Header的第一个字节
     * author blake
     * date 2020-10-02 21:12:37
     * param: fixedHeader
     * return byte
     **/
    public static byte encodeFixedHeaderByte1(MqttFixedHeader fixedHeader) {

        // bit 7 - 4 Packet type
        int number = fixedHeader.getMqttPacketType().getValue() << 4;

        // bit 3 DUP
        if (fixedHeader.isDUP()) {
            number |= 8;
        }

        // bit 2 - 1 QoS
        if(fixedHeader.getMqttPacketType().equals("PUBLISH")) {
            number |= fixedHeader.getMqttQoS().getValue() << 1;
        } else if (fixedHeader.getMqttPacketType().equals("PUBREL") ||
                fixedHeader.getMqttPacketType().equals("SUBSCRIBE") ||
                fixedHeader.getMqttPacketType().equals("UNSUBSCRIBE")
        ) {
            number |= 2;
        }

        // bit 0 RETAIN
        if (fixedHeader.isReain()) {
            number |= 1;
        }

        return (byte)number;
    }

    /**
     * description: 3.1.2 Connect Flags
     * author blake
     * date 2020-10-02 21:43:06
     * param: variableHeader
     * return byte
     **/
    public static byte encodeConnectVariableHeaderFlags(MqttConnectVariableHeader variableHeader) {

        int number = 0;

        // bit 7 User Name Flag
        if (variableHeader.isHasUserName()) {
            number |= 128;
        }

        // bit 6 Password Flag
        if (variableHeader.isHasPassword()) {
            number |=  64;
        }

        // bit 5 Will Retain
        if (variableHeader.isWillRerain()) {
            number |= 32;
        }

        // bit 4 - 3 Will QoS
        number |= (variableHeader.getMqttQoS().getValue() & 3) << 3;

        // bit 2
        if (variableHeader.isWillFlag()) {
            number |= 4;
        }

        // bit 1
        if (variableHeader.isCleanStat()) {
            number |= 2;
        }

        // bit 0 Reserved 0
        // number &= 0;

        return (byte)number;
    }
}
