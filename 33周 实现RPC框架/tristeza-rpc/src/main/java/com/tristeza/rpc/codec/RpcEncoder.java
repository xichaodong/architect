package com.tristeza.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author chaodong.xi
 * @date 2021/2/9 9:56 下午
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {
    private final Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (genericClass.isInstance(msg)) {
            byte[] data = Serialization.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
