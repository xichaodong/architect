package com.tristeza.handler;

import com.tristeza.protobuf.MessageModule;
import com.tristeza.task.MessageTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.*;

/**
 * @author chaodong.xi
 * @date 2021/1/27 下午4:48
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    ThreadPoolExecutor workerPool = new ThreadPoolExecutor(5, 10, 60L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(4000), new ThreadPoolExecutor.DiscardPolicy());

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.err.println("netty server start...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageModule.Message request = (MessageModule.Message) msg;
        workerPool.submit(new MessageTask(request, ctx));
    }
}
