package com.tristeza.service.impl;

import com.tristeza.annotation.Cmd;
import com.tristeza.annotation.Module;
import com.tristeza.protobuf.MessageModule;
import com.tristeza.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author chaodong.xi
 * @date 2021/1/28 下午12:53
 */
@Module(module = "user")
@Service
public class UserServiceImpl implements UserService {
    @Cmd(cmd = "save")
    @Override
    public void save(MessageModule.ResultType resultType, byte[] data) {
        if (MessageModule.ResultType.SUCCESS.equals(resultType)) {
            System.err.println("user module save success...");
        } else {
            System.err.println("user module save failed...");
        }
    }

    @Cmd(cmd = "update")
    @Override
    public void update(MessageModule.ResultType resultType, byte[] data) {
        if (MessageModule.ResultType.SUCCESS.equals(resultType)) {
            System.err.println("user module update success...");
        } else {
            System.err.println("user module update failed...");
        }
    }
}
