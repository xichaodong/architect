package com.chloe.controller;

import com.chloe.common.utils.CookieUtils;
import com.chloe.common.utils.JsonResult;
import com.chloe.common.utils.JsonUtils;
import com.chloe.common.utils.RedisOperator;
import com.chloe.model.pojo.Users;
import com.chloe.model.vo.UserVO;
import com.chloe.service.FileService;
import com.chloe.service.center.CenterUserService;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("file")
public class FileController {
    private static final Set<String> ADMIT_IMAGE_SUFFIX = ImmutableSet.of("jpg", "jpeg", "png");
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    private static final String USER_COOKIE_NAME = "user";
    private static final String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";

    @Resource
    private RedisOperator redisOperator;
    @Resource
    private FileService fileService;

    @PostMapping("upload")
    public JsonResult upload(@RequestParam String userId, MultipartFile file,
                             HttpServletRequest request, HttpServletResponse response) {
        if (Objects.nonNull(file)) {
            String filename = file.getOriginalFilename();

            if (StringUtils.isNotBlank(filename)) {
                String[] nameArrays = filename.split("\\.");
                String suffix = nameArrays[nameArrays.length - 1];

                if (!ADMIT_IMAGE_SUFFIX.contains(suffix)) {
                    return JsonResult.errorMsg("图片格式不正确，请重新上传");
                }

                try {
                    UserVO userVO = buildUserVO(fileService.upload(file, suffix, userId));
                    CookieUtils.setCookie(request, response, USER_COOKIE_NAME, JsonUtils.objectToJson(userVO), true);
                } catch (Exception e) {
                    LOGGER.error("文件上传失败", e);
                    return JsonResult.errorMsg("文件上传失败，请刷新或重新上传");
                }
            }
        } else {
            return JsonResult.errorMsg("文件不能为空");
        }

        return JsonResult.ok();
    }

    private UserVO buildUserVO(Users user) {
        String token = UUID.randomUUID().toString();
        redisOperator.set(String.format("%s:%s", REDIS_USER_TOKEN, user.getId()), token);

        UserVO userVO = new UserVO();

        BeanUtils.copyProperties(user, userVO);
        userVO.setUserUniqueToken(token);

        return userVO;
    }

}
