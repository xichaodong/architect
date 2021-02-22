package com.tristeza.user.api.center;

import com.tristeza.user.model.bo.center.CenterUserBO;
import com.tristeza.user.model.pojo.Users;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("center-user-api")
public interface CenterUserApi {
    /**
     * 根据id查询用户信息
     *
     * @param userId 用户id
     * @return {@link Users}
     */
    @GetMapping("profile")
    Users queryUserInfo(@RequestParam("userId") String userId);

    @PutMapping("profile/{userId}")
    Users updateUserInfo(@PathVariable("userId") String userId, @RequestBody CenterUserBO centerUserBO);

    //TODO
    @PostMapping("headImg")
    Users uploadUserFace(@RequestParam("userId") String userId, MultipartFile multipartFile, String fileSavePath, String imgServerUrl);
}
