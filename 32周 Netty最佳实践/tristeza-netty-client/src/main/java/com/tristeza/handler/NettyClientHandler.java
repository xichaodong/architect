package com.tristeza.handler;

import com.tristeza.protobuf.MessageModule;
import com.tristeza.task.MessageTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2021/1/27 下午4:32
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    ThreadPoolExecutor workerPool = new ThreadPoolExecutor(5, 10, 60L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(4000), new ThreadPoolExecutor.DiscardPolicy());

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.err.println("netty client start...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageModule.Message response = (MessageModule.Message) msg;
        workerPool.submit(new MessageTask(response, ctx));
    }
}
