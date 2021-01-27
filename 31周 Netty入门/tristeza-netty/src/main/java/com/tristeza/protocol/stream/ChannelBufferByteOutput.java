package com.tristeza.protocol.stream;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

/**
 * @author chaodong.xi
 * @date 2021/1/26 下午10:42
 */
public class ChannelBufferByteOutput implements ByteOutput {
    private final ByteBuf buffer;

    /**
     * Create a new instance which use the given {@link ByteBuf}
     */
    public ChannelBufferByteOutput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    @Override
    public void close() {
        // Nothing to do
    }

    @Override
    public void flush() {
        // nothing to do
    }

    @Override
    public void write(int b) {
        buffer.writeByte(b);
    }

    @Override
    public void write(byte[] bytes) {
        buffer.writeBytes(bytes);
    }

    @Override
    public void write(byte[] bytes, int srcIndex, int length) {
        buffer.writeBytes(bytes, srcIndex, length);
    }

    /**
     * Return the {@link ByteBuf} which contains the written content
     */
    ByteBuf getBuffer() {
        return buffer;
    }
}
