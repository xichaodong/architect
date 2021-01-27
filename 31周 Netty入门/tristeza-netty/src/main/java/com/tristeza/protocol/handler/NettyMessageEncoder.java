package com.tristeza.protocol.handler;

import com.tristeza.protocol.marshalling.MarshallingEncoder;
import com.tristeza.protocol.struct.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2021/1/26 下午8:49
 */
public class NettyMessageEncoder extends MessageToByteEncoder<Protocol> {
    private final MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, Protocol protocol, ByteBuf byteBuf) throws Exception {
        if (Objects.isNull(protocol) || Objects.isNull(protocol.getHeader())) {
            throw new Exception("encode message is null");
        }

        //crc => 4byte
        byteBuf.writeInt(protocol.getHeader().getCrcCode());

        //messageLength => 4byte
        byteBuf.writeInt(protocol.getHeader().getLength());

        //sessionId => 8byte
        byteBuf.writeLong(protocol.getHeader().getSessionId());

        //messageType => 1byte
        byteBuf.writeByte(protocol.getHeader().getType());

        //messagePriority => 1byte
        byteBuf.writeByte(protocol.getHeader().getPriority());

        //messageSize => 4byte
        byteBuf.writeInt(protocol.getHeader().getAttachment().size());

        byte[] keyArray;
        Object value;

        for (Map.Entry<String, Object> param : protocol.getHeader().getAttachment().entrySet()) {
            keyArray = param.getKey().getBytes(StandardCharsets.UTF_8);
            byteBuf.writeInt(keyArray.length);
            byteBuf.writeBytes(keyArray);
            value = param.getValue();
            this.marshallingEncoder.encode(value, byteBuf);
        }

        if (Objects.nonNull(protocol.getBody())) {
            marshallingEncoder.encode(protocol.getBody(), byteBuf);
        } else {
            byteBuf.writeInt(0);
        }

        //写入数据长度 = 总当前数据长度 - crcCode - length
        byteBuf.setInt(4, byteBuf.readableBytes() - 4 - 4);
    }
}
