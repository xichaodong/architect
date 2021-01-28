package com.tristeza.service.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.tristeza.annotation.Cmd;
import com.tristeza.annotation.Module;
import com.tristeza.model.Response;
import com.tristeza.protobuf.UserModule;
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
    public Response<?> save(byte[] data) {
        UserModule.User user;
        try {
            user = UserModule.User.parseFrom(data);
            System.err.println("save finish, userId: " + user.getUserId() + ",userName: " + user.getUserName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return Response.failure();
        }
        return Response.success(user);
    }

    @Cmd(cmd = "update")
    @Override
    public Response<?> update(byte[] data) {
        UserModule.User user;
        try {
            user = UserModule.User.parseFrom(data);
            System.err.println("update finish, userId: " + user.getUserId() + ",userName: " + user.getUserName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return Response.failure();
        }
        return Response.success(user);
    }
}
