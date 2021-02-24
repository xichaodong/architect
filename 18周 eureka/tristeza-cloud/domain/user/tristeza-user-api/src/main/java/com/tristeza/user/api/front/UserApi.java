package com.tristeza.user.api.front;

import com.tristeza.user.model.bo.UserBO;
import com.tristeza.user.model.pojo.Users;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@RequestMapping("user-api")
@FeignClient("user-api")
public interface UserApi {
    /**
     * 判断用户名是否存在
     */
    @GetMapping("user/exist")
    boolean queryUsernameIsExist(@RequestParam("username") String username);

    /**
     * 创建用户
     *
     * @param userBo {@link UserBO}
     * @return {@link Users}
     */
    @PostMapping("user")
    Users createUser(@RequestBody UserBO userBo);

    /**
     * 登录
     */
    @GetMapping("user/verify")
    Users queryUserForLogin(@RequestParam("username") String username, @RequestParam("password") String password);
}
