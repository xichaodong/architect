package com.tristeza.task;

import com.tristeza.builder.MessageBuilder;
import com.tristeza.model.Response;
import com.tristeza.protobuf.MessageModule;
import com.tristeza.scanner.Invoker;
import com.tristeza.scanner.InvokerTable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

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
        try {
            String module = message.getModule();
            String cmd = message.getCmd();
            MessageModule.ResultType resultType = message.getResultType();

            byte[] data = message.getBody().toByteArray();

            Invoker invoker = InvokerTable.getInvoker(module, cmd);
            invoker.invoke(resultType, data);
        } finally {
            ReferenceCountUtil.release(message);
        }
    }
}
