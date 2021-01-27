package com.tristeza.netty.client;

import com.tristeza.common.data.Request;
import com.tristeza.common.data.Response;
import com.tristeza.utils.GzipUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author chaodong.xi
 * @date 2021/1/24 下午11:24
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client channel active");
        Channel channel = ctx.channel();
        for (int i = 0; i < 1; i++) {
            Request request = new Request();
            request.setId(String.valueOf(i));
            request.setName("msg:" + i);
            request.setRequestMessage("内容:" + i);
            String path = NettyClient.class.getClassLoader().getResource("./").toURI().getPath() + File.separator + "cat.jpg";
            FileInputStream fis = new FileInputStream(new File(path));
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            request.setAttachment(GzipUtils.gzip(data));
            channel.writeAndFlush(request);
        }
    }

    /**
     * channelRead
     * 真正的数据最终会走到这个方法进行处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            Response response = (Response) msg;
            System.out.println("收到服务端消息 => id:" + response.getId() + ",name:" + response.getName() + ",requestMessage:" + response.getResponseMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
