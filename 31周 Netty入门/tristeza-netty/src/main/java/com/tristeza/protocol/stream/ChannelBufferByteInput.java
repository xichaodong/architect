package com.tristeza.protocol.stream;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;

/**
 * @author chaodong.xi
 * @date 2021/1/26 下午10:43
 */
public class ChannelBufferByteInput implements ByteInput {
    private final ByteBuf buffer;

    public ChannelBufferByteInput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    @Override
    public void close() {
        // nothing to do
    }

    @Override
    public int available() {
        return buffer.readableBytes();
    }

    @Override
    public int read() {
        if (buffer.isReadable()) {
            return buffer.readByte() & 0xff;
        }
        return -1;
    }

    @Override
    public int read(byte[] array) {
        return read(array, 0, array.length);
    }

    @Override
    public int read(byte[] dst, int dstIndex, int length) {
        int available = available();
        if (available == 0) {
            return -1;
        }

        length = Math.min(available, length);
        buffer.readBytes(dst, dstIndex, length);
        return length;
    }

    @Override
    public long skip(long bytes) {
        int readable = buffer.readableBytes();
        if (readable < bytes) {
            bytes = readable;
        }
        buffer.readerIndex((int) (buffer.readerIndex() + bytes));
        return bytes;
    }
}
