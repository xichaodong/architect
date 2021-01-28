package com.tristeza.task;

import com.tristeza.builder.MessageBuilder;
import com.tristeza.model.Response;
import com.tristeza.protobuf.MessageModule;
import com.tristeza.scanner.Invoker;
import com.tristeza.scanner.InvokerTable;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author chaodong.xi
 * @date 2021/1/28 下午12:28
 */
public class MessageTask implements Runnable {
    private final MessageModule.Message message;
    private final ChannelHandlerContext context;

    public MessageTask(MessageModule.Message message, ChannelHandlerContext context) {
        this.message = message;
        this.context = context;
    }

    @Override
    public void run() {
        String module = message.getModule();
        String cmd = message.getCmd();
        byte[] data = message.getBody().toByteArray();

        Invoker invoker = InvokerTable.getInvoker(module, cmd);
        Response<?> response = (Response<?>) invoker.invoke(data);

        context.channel().writeAndFlush(MessageBuilder.getResponseMessage(module, cmd,
                response.getResultType(), response.getContent()));
    }
}
