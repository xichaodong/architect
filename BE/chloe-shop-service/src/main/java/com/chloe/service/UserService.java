package com.chloe.service;

import com.chloe.model.bo.UserBo;
import com.chloe.model.pojo.Users;

public interface UserService {
    /**
     * 判断用户名是否存在
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     *
     * @param userBo {@link UserBo}
     * @return {@link Users}
     */
    Users createUser(UserBo userBo);

    /**
     * 登录
     *
     * @param userBo {@link UserBo}
     * @return {@link Users}
     */
    Users queryUserForLogin(UserBo userBo);
}
