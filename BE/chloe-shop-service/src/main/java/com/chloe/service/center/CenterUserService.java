package com.chloe.service.center;

import com.chloe.model.bo.center.CenterUserBO;
import com.chloe.model.pojo.Users;

public interface CenterUserService {
    /**
     * 根据id查询用户信息
     *
     * @param userId 用户id
     * @return {@link Users}
     */
    Users queryUserInfo(String userId);

    Users updateUserInfo(String userId, CenterUserBO centerUserBO);
}
