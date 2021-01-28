package com.tristeza.controller;

import com.tristeza.client.NettyClient;
import com.tristeza.protobuf.UserModule;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chaodong.xi
 * @date 2021/1/28 下午3:41
 */
@RestController
public class TestController {
    @GetMapping("save")
    public String save() {
        NettyClient.getInstance().sendMessage("user", "save",
                UserModule.User.newBuilder().setUserId("001").setUserName("Tristeza").build());
        return "success";
    }

    @GetMapping("update")
    public String update() {
        NettyClient.getInstance().sendMessage("user", "update",
                UserModule.User.newBuilder().setUserId("002").setUserName("Tristeza").build());
        return "success";
    }
}
