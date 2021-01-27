package com.tristeza.netty.server;

import com.tristeza.common.data.Request;
import com.tristeza.common.data.Response;
import com.tristeza.netty.client.NettyClient;
import com.tristeza.utils.GzipUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author chaodong.xi
 * @date 2021/1/24 下午11:20
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("server channel active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        System.out.println("收到客户端消息 => id:" + request.getId() + ",name:" + request.getName() + ",requestMessage:" + request.getRequestMessage());
        byte[] attachment = GzipUtils.ungzip(request.getAttachment());
        String path = NettyClient.class.getClassLoader().getResource("./").toURI().getPath()
                + File.separator + "01.jpg";
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(attachment);
        fos.close();

        Response response = new Response();
        response.setId("response " + request.getId());
        response.setName("response " + request.getName());
        response.setResponseMessage("响应信息");

        ctx.writeAndFlush(response);
    }
}
