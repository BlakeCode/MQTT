package mqtt;

import java.io.*;

/**
 * @author blake
 * date 2020-10-02 17:59:27
 **/

public class MqttEncoder {

    public static byte[] encode(MqttPacket packet) throws Exception{
        switch (packet.getMqttFixedHeader().getMqttPacketType()) {
            case CONNECT:
                return encodeConnectPacket((MqttConnectPacket)packet);
            case CONNACK:
                return encodeConnAckPacket((MqttConnAckPacket)packet);
            case PUBLISH:
                return encodePublishPacket((MqttPublishPacket)packet);
            case PUBACK:
            case PUBREC:
            case PUBREL:
            case PUBCOMP:
                return encodePubAckPacket((MqttPubAckPacket)packet);
            case SUBSCRIBE:
                return encodeSubscribePacket((MqttSubscribePacket)packet);
            case SUBACK:
                return encodeSubAckPacket((MqttSubAckPacket)packet);
            case UNSUBSCRIBE:
                return encodeUnSubscribePacket((MqttUnSubscribePacket)packet);
            default:
                throw new IllegalArgumentException("Unknown packet type: " +
                        packet.getMqttFixedHeader().getMqttPacketType()
                );
        }
    }

    /**
     * description: encode CONNECT packet
     * @author blake
     * date 2020-10-02 21:17:35
     * @param packet
     * @return byte[]
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
        byte[] connectPropertyBytesLength = MqttUtil.encodeIntToVariableBytes(connectPropertyBytes.length);
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
            byte[] willPropertyBytesLength = MqttUtil.encodeIntToVariableBytes(willPropertyBytes.length);
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

        fixedHeader.setRemainingLength(variableByteSize + payloadByteSize);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // write the 2nd byte of Fixed Header
        if(fixedHeader.getRemainingLength() != variableByteSize + payloadByteSize) {
            throw new Exception("Reamaining Length error");
        } else {
            bos.write(MqttUtil.encodeIntToVariableBytes(fixedHeader.getRemainingLength()));
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

    /**
     * description: encode CONNACK packet
     * @author blake
     * date   2020-10-17 15:52:50
     * @param packet
     * @return byte[]
     **/
    public static byte[] encodeConnAckPacket(MqttConnAckPacket packet) throws Exception {

        // Fixed + Variable
        MqttFixedHeader fixedHeader = packet.getConnAckFixedHeader();
        MqttConnAckVariableHeader variableHeader = packet.getConnAckVariableHeader();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 3.2.2.1 Session Present
        byte sessionPresent = (byte)(variableHeader.isSessionPresent() ? 1 : 0);
        // 3.2.2.2 Connect Reason Code
        byte connectReasonCode = variableHeader.getConnectReasonCode().getValue();
        // 3.2.2.3 Connack Properties
        byte[] connackProperties = encodeProperties(variableHeader.getConnackProperties());
        byte[] connackPropertiesLength = MqttUtil.encodeIntToVariableBytes(connackProperties.length);

        // 3.2.1 write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // 3.2.1 write the 2nd byte of Fixed Header
        bos.write(MqttUtil.encodeIntToVariableBytes(2 + connackPropertiesLength.length +
                connackProperties.length));

        // 3.2.2 write Connack Variable Header
        bos.write(sessionPresent);
        bos.write(connectReasonCode);
        bos.write(connackPropertiesLength);
        bos.write(connackProperties);

        return bos.toByteArray();
    }

    /**
     * description: encode PUBLISH packet
     * @author blake
     * date   2020-10-17 15:55:29
     * @param packet
     * @return byte[]
     **/
    public static byte[] encodePublishPacket(MqttPublishPacket packet) throws Exception {

        // Fixed Header + Variable Header + Payload
        MqttFixedHeader fixedHeader = packet.getPublishFixedHeader();
        MqttPublishVariableHeader variableHeader = packet.getPublishVariableHeader();
        byte[] payload = packet.getPublishPayload();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 3.3.2 Publish Variable Header
        // 3.3.2.1 Topic Name
        byte[] topicNameBytes = MqttUtil.encodeStringToUTF8String(variableHeader.getTopicName());
        // 3.3.2.2 Packet Identifier
        byte[] packetIdentifier = null;
        if (fixedHeader.getMqttQoS().getValue() > 0) {
            packetIdentifier = MqttUtil.encodeShortToTwoByte(variableHeader.getPacketIdentifier());
        }
        // 3.3.2.3 PUBLISH Properties
        byte[] publishProperties = encodeProperties(variableHeader.getPublishProperties());
        byte[] publishPropertiesLength = MqttUtil.encodeIntToVariableBytes(publishProperties.length);
        int variableHeaderByteSize = topicNameBytes.length + packetIdentifier.length +
                publishProperties.length + publishPropertiesLength.length;

        // 3.3.1 write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // 3.3.1 write the 2nd byte of Fixed Header
        bos.write(MqttUtil.encodeIntToVariableBytes(variableHeaderByteSize + payload.length));
        // 3.3.2 write the Variable Header
        bos.write(topicNameBytes);
        bos.write(packetIdentifier);
        bos.write(publishPropertiesLength);
        bos.write(publishProperties);
        // 3.3.3 write the Payload
        bos.write(payload);

        return bos.toByteArray();

    }

    /**
     * description: encode PubAck packet
     * @author blake
     * date   2020-10-17 19:32:35
     * @param packet
     * @return byte[]
     **/
    public static byte[] encodePubAckPacket(MqttPubAckPacket packet) throws Exception {

        // Fixed Header + Variable Header
        MqttFixedHeader fixedHeader = packet.getPubAckFixedHeader();
        MqttPubAckVariableHeader variableHeader = packet.getPubAckVariableHeader();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 3.4.2 PUBACK Variable Header
        byte[] packetIdentifier = MqttUtil.encodeShortToTwoByte(variableHeader.getPacketIdentifier());
        byte reasonCode = variableHeader.getReasonCode().getValue();
        byte[] properties = encodeProperties(variableHeader.getProperties());
        byte[] propertiesLength = MqttUtil.encodeIntToVariableBytes(properties.length);
        int variableHeaderByteSize = packetIdentifier.length + 1 +
                propertiesLength.length + properties.length;

        // 3.4.1 write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // 3.4.1 write the 2nd byte of Fixed Header
        bos.write(MqttUtil.encodeIntToVariableBytes(variableHeaderByteSize + 0));
        // 3.4.2 write PUBACK Variable Header
        bos.write(packetIdentifier);
        bos.write(reasonCode);
        bos.write(propertiesLength);
        bos.write(properties);

        return bos.toByteArray();
    }

    /**
     * description: encode Subscribe packet
     * @author blake
     * date   2020-10-18 20:16:01
     * @param packet
     * @return byte[]
     **/
    public static byte[] encodeSubscribePacket(MqttSubscribePacket packet) throws Exception {

        // Fixed Header + Variable Header + Payload
        MqttFixedHeader fixedHeader = packet.getSubscribeFixedHeader();
        MqttSubscribeVariableHeader variableHeader = packet.getSubscribeVariableHeader();
        MqttSubscribePayload payload = packet.getSubscribePayload();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 3.8.2 SUBSCRIBE Variable Header
        byte[] packetIdentifier = MqttUtil.encodeShortToTwoByte(variableHeader.getPacketIdentifier());
        // 3.8.2 SUBSCRIBE Variable Header
        byte[] properties = encodeProperties(variableHeader.getProperties());
        byte[] propertiesLength = MqttUtil.encodeIntToVariableBytes(properties.length);
        int variableHeaderByteSize = packetIdentifier.length + propertiesLength.length + properties.length;

        // 3.8.3 SUBSCRIBE Payload
        ByteArrayOutputStream subscribepayload = new ByteArrayOutputStream();
        for (MqttTopicSubscription subscription : payload.getTopicSubscriptionList()) {
            subscribepayload.write(MqttUtil.encodeStringToUTF8String(subscription.getTopicFilter()));
            subscribepayload.write(encodeSubscriptionOptions(subscription));
        }
        byte[] topicFilter = subscribepayload.toByteArray();
        int payloadByteSize = topicFilter.length;

        // 3.8.1 write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // 3.8.1 write the 2nd byte of Fixed Header
        bos.write(MqttUtil.encodeIntToVariableBytes(variableHeaderByteSize + payloadByteSize));
        // 3.8.2 write SUBSCRIBE Variable Header
        bos.write(packetIdentifier);
        bos.write(propertiesLength);
        bos.write(properties);
        // 3.8.3 write SUBSCRIBE Payload
        bos.write(topicFilter);

        return bos.toByteArray();

    }

    /**
     * description: encode SubAck packet
     * @author blake
     * date   2020-10-18 20:52:56
     * @param packet
     * @return byte[]
     **/
    public static byte[] encodeSubAckPacket(MqttSubAckPacket packet) throws Exception {

        // Fixed Header + Variable Header + Payload
        MqttFixedHeader fixedHeader = packet.getSubAckFixedHeader();
        MqttSubAckVariableHeader variableHeader = packet.getSubAckVariableHeader();
        MqttSubAckPayload payload = packet.getSubAckPayload();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 3.9.2 write SUBACK Variable Header
        byte[] packetIdentifier = MqttUtil.encodeShortToTwoByte(variableHeader.getPacketIdentifier());
        byte[] properties = encodeProperties(variableHeader.getProperties());
        byte[] propertiesLength = MqttUtil.encodeIntToVariableBytes(properties.length);
        int variableHeaderByteSize = packetIdentifier.length + propertiesLength.length + properties.length;

        // 3.9.3 write SUBACK Payload
        int payloadByteSize = payload.getReasonCodeList().size();

        // 3.9.1 write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // 3.9.1 write the 2nd byte of Fixed Header
        bos.write(MqttUtil.encodeIntToVariableBytes(variableHeaderByteSize + payloadByteSize));
        // 3.9.2 write SUBACK Variable Header
        bos.write(packetIdentifier);
        bos.write(propertiesLength);
        bos.write(properties);
        // 3.9.3 write SUBACK Payload
        for(MqttSubscribeReasonCode reasonCode : payload.getReasonCodeList()) {
            bos.write(reasonCode.getValue());
        }

        return bos.toByteArray();
    }

    /**
     * description: encode UNSUBSCRIBE packet
     * @author blake
     * date   2020-10-19 16:30:37
     * @param packet
     * @return byte[]
     **/
    public static byte[] encodeUnSubscribePacket(MqttUnSubscribePacket packet) throws Exception {

        // Fixed Header + Variable Header + Payload
        MqttFixedHeader fixedHeader = packet.getUnSubscribeFixedHeader();
        // SUBSCRIBE Variable Format == UNSUBSCRIBE Variable Format
        MqttSubscribeVariableHeader variableHeader = packet.getUnSubscribeVariableHeader();
        // SUBSCRIBE Payload == UNSUBSCRIBE Payload
        MqttSubscribePayload payload = packet.getUnSubscribePayload();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 3.10.2 UNSUBSCRIBE Variable Header
        byte[] packetIdentifier = MqttUtil.encodeShortToTwoByte(variableHeader.getPacketIdentifier());
        // 3.10.2 UNSUBSCRIBE Variable Header
        byte[] properties = encodeProperties(variableHeader.getProperties());
        byte[] propertiesLength = MqttUtil.encodeIntToVariableBytes(properties.length);
        int variableHeaderByteSize = packetIdentifier.length + propertiesLength.length + properties.length;

        // 3.10.3 UNSUBSCRIBE Payload
        ByteArrayOutputStream subscribepayload = new ByteArrayOutputStream();
        for (MqttTopicSubscription subscription : payload.getTopicSubscriptionList()) {
            subscribepayload.write(MqttUtil.encodeStringToUTF8String(subscription.getTopicFilter()));
        }
        byte[] topicFilter = subscribepayload.toByteArray();
        int payloadByteSize = topicFilter.length;

        // 3.10.1 write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // 3.10.1 write the 2nd byte of Fixed Header
        bos.write(MqttUtil.encodeIntToVariableBytes(variableHeaderByteSize + payloadByteSize));
        // 3.10.2 write UNSUBSCRIBE Variable Header
        bos.write(packetIdentifier);
        bos.write(propertiesLength);
        bos.write(properties);
        // 3.10.3 write UNSUBSCRIBE Payload
        bos.write(topicFilter);

        return bos.toByteArray();
    }

    /**
     * description: encode UNSUBACK packet
     * @author blake
     * date   2020-10-19 19:36:38
     * @param packet
     * @return byte[]
     **/
    public static byte[] encodeUnSubAckPacket(MqttUnSubAckPacket packet) throws Exception {

        // Fixed Header + Variable Header + Payload
        MqttFixedHeader fixedHeader = packet.getUnSubAckFixedHeader();
        MqttSubAckVariableHeader variableHeader = packet.getUnSubAckVariableHeader();
        MqttSubAckPayload payload = packet.getUnSubAckPayload();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 3.11.2 UNSUBACK Variable Header
        byte[] packetIdentifier = MqttUtil.encodeShortToTwoByte(variableHeader.getPacketIdentifier());
        byte[] properties = encodeProperties(variableHeader.getProperties());
        byte[] propertiesLength = MqttUtil.encodeIntToVariableBytes(properties.length);
        int variableHeaderByteSize = packetIdentifier.length + propertiesLength.length + properties.length;

        // 3.11.3 UNSUBACK Payload
        int payloadByteSize = payload.getReasonCodeList().size();

        // 3.11.1 write the 1st byte of Fixed Header
        bos.write(encodeFixedHeaderByte1(fixedHeader));
        // 3.11.1 write the 2nd byte of Fixed Header
        bos.write(MqttUtil.encodeIntToVariableBytes(variableHeaderByteSize + payloadByteSize));
        // 3.11.2 write UNSUBACK Variable Header
        bos.write(packetIdentifier);
        bos.write(propertiesLength);
        bos.write(properties);
        // 3.11.3 write UNSUBACK Payload
        for(MqttSubscribeReasonCode reasonCode : payload.getReasonCodeList()) {
            bos.write(reasonCode.getValue());
        }

        return bos.toByteArray();
    }

    /**
     * description: encode Properties to byte[]
     * @author blake
     * date 2020-10-07 18:02:30
     * @param mqttProperties
     * @return byte[]
     **/
    public static byte[] encodeProperties(MqttProperties mqttProperties) {

        if (mqttProperties == null) {
            return null;
        }

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
                        bos.write(MqttUtil.encodeIntToVariableBytes((Integer) mqttProperty.getValue()));
                        break;

                    // UTF-8 String Pair
                    case USER_PROPERTY:
                        break;

                    default:
                        throw new Exception("Unknown property type: ");
                }
            }
        } catch (Exception e) {
                System.out.println("NULL Properties in func encodeProperties() : " + e);
        }

        return bos.toByteArray();
    }

    /**
     * description: 2.1.3 编码 Fixed Header的第一个字节
     * @author blake
     * date 2020-10-02 21:12:37
     * @param fixedHeader
     * @return byte
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
     * @author blake
     * date 2020-10-02 21:43:06
     * @param variableHeader
     * @return byte
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


    public static byte encodeSubscriptionOptions(MqttTopicSubscription topicSubscription) {

        int number = 0;

        // byte 0 - 1
        number |= (topicSubscription.getMqttQoS().getValue()) & 3;

        // byte 2
        number |= topicSubscription.isNoLocal() ? 4 : 0;

        // byte 3
        number |= topicSubscription.isRetainAsPublished() ? 8 : 0;

        // byte 4 - 5
        number |= (topicSubscription.getRetainhandlingOption() & 3) << 4;

        // byte 6 - 7
        // number &= 0xC0

        return (byte)number;
    }
}
