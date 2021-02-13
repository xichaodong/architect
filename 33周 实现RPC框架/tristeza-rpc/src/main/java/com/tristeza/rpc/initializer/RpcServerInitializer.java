package com.tristeza.rpc.initializer;

import com.tristeza.rpc.codec.RpcDecoder;
import com.tristeza.rpc.codec.RpcEncoder;
import com.tristeza.rpc.handler.RpcClientHandler;
import com.tristeza.rpc.handler.RpcServerHandler;
import com.tristeza.rpc.model.RpcRequest;
import com.tristeza.rpc.model.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Map;

/**
 * @author chaodong.xi
 * @date 2021/2/9 10:39 下午
 */
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {
    private final Map<String, Object> handlerMap;

    public RpcServerInitializer(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new RpcEncoder(RpcResponse.class));
        ch.pipeline().addLast(new RpcDecoder(RpcRequest.class));
        ch.pipeline().addLast(new RpcServerHandler(handlerMap));
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 0));
    }
}
