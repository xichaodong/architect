package com.tristeza.rpc.handler;

import com.tristeza.rpc.model.RpcRequest;
import com.tristeza.rpc.model.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2021/2/9 10:38 下午
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handlerMap;
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16,
            600L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(65535));

    public RpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        executor.submit(() -> {
            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());
            try {
                response.setResult(handler(request));
            } catch (Throwable t) {
                response.setThrowable(t);
                LOGGER.error("RPC业务逻辑执行异常，原因 = ", t);
            }
            ctx.writeAndFlush(response).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {

                }
            });
        });
    }

    private Object handler(RpcRequest request) throws InvocationTargetException {
        String className = request.getClassName();
        Object ref = handlerMap.get(className);

        if (Objects.isNull(ref)) {
            return null;
        }

        FastClass fastClass = FastClass.create(ref.getClass());
        FastMethod fastMethod = fastClass.getMethod(request.getMethodName(), request.getParameterTypes());

        return fastMethod.invoke(ref, request.getParameters());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
