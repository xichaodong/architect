package com.tristeza.netty.client;

import com.tristeza.netty.common.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author chaodong.xi
 * @date 2021/1/24 下午11:24
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * channelActive
     * 客户端通道激活
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("client channel active..");
    }

    /**
     * channelRead
     * 真正的数据最终会走到这个方法进行处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 固定模式的 try .. finally
        // 在try代码片段处理逻辑, finally进行释放缓存资源, 也就是 Object msg (buffer)
        try {
            ByteBuf buf = (ByteBuf) msg;
            //创建目标大小的数组
            byte[] bytes = new byte[buf.readableBytes()];
            //把数据从buf转移到byte[]
            buf.getBytes(0, bytes);

            System.out.println(new String(bytes));
            System.out.flush();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * exceptionCaught
     * 异常捕获方法
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
