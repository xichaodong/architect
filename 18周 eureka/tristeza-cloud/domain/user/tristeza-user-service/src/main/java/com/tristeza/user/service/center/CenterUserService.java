package com.tristeza.user.service.center;

import com.tristeza.user.model.bo.center.CenterUserBO;
import com.tristeza.user.model.pojo.Users;
import org.springframework.web.multipart.MultipartFile;

public interface CenterUserService {
    /**
     * 根据id查询用户信息
     *
     * @param userId 用户id
     * @return {@link Users}
     */
    Users queryUserInfo(String userId);

    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    Users uploadUserFace(String userId, MultipartFile multipartFile, String fileSavePath, String imgServerUrl);
}
