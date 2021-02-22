package com.tristeza.user.service.front;

import com.tristeza.user.model.bo.UserBO;
import com.tristeza.user.model.pojo.Users;

public interface UserService {
    /**
     * 判断用户名是否存在
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     *
     * @param userBo {@link UserBO}
     * @return {@link Users}
     */
    Users createUser(UserBO userBo);

    /**
     * 登录
     */
    Users queryUserForLogin(String username, String password);
}
