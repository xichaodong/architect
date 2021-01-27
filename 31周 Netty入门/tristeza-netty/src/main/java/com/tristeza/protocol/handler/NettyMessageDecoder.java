package com.tristeza.protocol.handler;

import com.tristeza.protocol.marshalling.MarshallingDecoder;
import com.tristeza.protocol.struct.Header;
import com.tristeza.protocol.struct.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2021/1/27 上午11:09
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
    private final MarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buffer = (ByteBuf) super.decode(ctx, in);

        if (Objects.isNull(buffer)) {
            return null;
        }

        Protocol protocol = new Protocol();

        protocol.setHeader(decodeHeader(buffer));

        if (buffer.readableBytes() > 4) {
            protocol.setBody(marshallingDecoder.decode(buffer));
        }

        return super.decode(ctx, in);
    }

    private Header decodeHeader(ByteBuf buffer) throws Exception {
        Header header = new Header();

        header.setCrcCode(buffer.readInt());
        header.setLength(buffer.readInt());
        header.setSessionId(buffer.readLong());
        header.setType(buffer.readByte());
        header.setPriority(buffer.readByte());
        header.setAttachment(decodeAttachment(buffer));

        return header;
    }

    private Map<String, Object> decodeAttachment(ByteBuf buffer) throws Exception {
        Map<String, Object> attachment = new HashMap<>();

        int size = buffer.readInt();

        if (size > 0) {
            byte[] keyArray;
            for (int i = 0; i < size; i++) {
                keyArray = new byte[buffer.readInt()];
                buffer.readBytes(keyArray);
                attachment.put(new String(keyArray, StandardCharsets.UTF_8), marshallingDecoder.decode(buffer));
            }
        }

        return attachment;
    }
}
