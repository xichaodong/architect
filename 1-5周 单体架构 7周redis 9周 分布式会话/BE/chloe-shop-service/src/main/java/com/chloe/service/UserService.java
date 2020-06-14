package com.chloe.service;

import com.chloe.model.bo.UserBO;
import com.chloe.model.pojo.Users;

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
     *
     * @param userBo {@link UserBO}
     * @return {@link Users}
     */
    Users queryUserForLogin(UserBO userBo);
}
