package com.tristeza.builder;

import com.google.protobuf.GeneratedMessageV3;
import com.tristeza.protobuf.MessageModule;

/**
 * @author chaodong.xi
 * @date 2021/1/28 下午2:37
 */
public class MessageBuilder {
    private static final int CRC_CODE = 0xabef0101;

    public static MessageModule.Message getRequestMessage(String module, String cmd,
                                                          GeneratedMessageV3 data) {
        return MessageModule.Message.newBuilder()
                .setCrcCode(CRC_CODE)
                .setMessageType(MessageModule.MessageType.REQUEST)
                .setModule(module)
                .setCmd(cmd)
                .setBody(data.toByteString())
                .build();
    }

    public static MessageModule.Message getResponseMessage(String module, String cmd,
                                                           MessageModule.ResultType resultType,
                                                           GeneratedMessageV3 data) {
        return MessageModule.Message.newBuilder()
                .setCrcCode(CRC_CODE)
                .setMessageType(MessageModule.MessageType.RESPONSE)
                .setModule(module)
                .setCmd(cmd)
                .setResultType(resultType)
                .setBody(data.toByteString())
                .build();
    }
}
