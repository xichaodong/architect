package com.tristeza.user.web.controller.center;

import com.google.common.collect.ImmutableSet;
import com.tristeza.cloud.common.utils.CookieUtils;
import com.tristeza.cloud.common.utils.DateUtil;
import com.tristeza.cloud.common.utils.JsonUtils;
import com.tristeza.cloud.common.utils.RedisOperator;
import com.tristeza.cloud.model.pojo.JsonResult;
import com.tristeza.user.model.bo.center.CenterUserBO;
import com.tristeza.user.model.pojo.Users;
import com.tristeza.user.model.vo.UserVO;
import com.tristeza.user.service.center.CenterUserService;
import com.tristeza.user.web.resource.FileUpload;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("userInfo")
public class CenterUserController {
    private static final Set<String> ADMIT_IMAGE_SUFFIX = ImmutableSet.of("jpg", "jpeg", "png");
    private static final String USER_IMAGE_LOCATION = String.format("..%sface", File.separator);
    private static final String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";
    private static final String USER_COOKIE_NAME = "user";

    @Resource
    private CenterUserService centerUserService;
    @Resource
    private RedisOperator redisOperator;
    @Resource
    private FileUpload fileUpload;

    @ApiOperation(value = "用户信息修改", notes = "用户信息修改", httpMethod = "POST")
    @PostMapping("update")
    public JsonResult updateUserInfo(@RequestParam String userId,
                                     @RequestBody @Valid CenterUserBO centerUserBO,
                                     BindingResult bindingResult,
                                     HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return JsonResult.errorMap(getErrors(bindingResult));
        }

        Users users = centerUserService.updateUserInfo(userId, centerUserBO);

        UserVO userVO = buildUserVO(users);

        CookieUtils.setCookie(request, response, USER_COOKIE_NAME, JsonUtils.objectToJson(userVO), true);

        return JsonResult.ok();
    }

    @ApiOperation(value = "用户头像上传", notes = "用户头像上传", httpMethod = "POST")
    @PostMapping("uploadFace")
    public JsonResult updateUserFace(@RequestParam String userId, MultipartFile file,
                                     HttpServletRequest request, HttpServletResponse response) {
        if (Objects.nonNull(file)) {
            String uploadPathPrefix = String.format("%s%s", File.separator, userId);
            String filename = file.getOriginalFilename();

            if (StringUtils.isNotBlank(filename)) {
                String[] nameArrays = filename.split("\\.");
                String suffix = nameArrays[nameArrays.length - 1];

                if (!ADMIT_IMAGE_SUFFIX.contains(suffix)) {
                    return JsonResult.errorMsg("图片格式不正确");
                }

                String newFilename = String.format("face-%s.%s", userId, suffix);
                String fileSavePath = String.format("%s%s%s%s", USER_IMAGE_LOCATION, uploadPathPrefix, File.separator, newFilename);
                String imgServerUrl = String.format("%s%s%s%s?t=%s", fileUpload.getImgServerUrl(), userId, File.separator,
                        newFilename, DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN));

                Users users = centerUserService.uploadUserFace(userId, file, fileSavePath, imgServerUrl);

                UserVO userVO = buildUserVO(users);
                CookieUtils.setCookie(request, response, USER_COOKIE_NAME, JsonUtils.objectToJson(userVO), true);
            }
        } else {
            return JsonResult.errorMsg("文件不能为空");
        }

        return JsonResult.ok();
    }

    Map<String, String> getErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> StringUtils.defaultIfBlank(fieldError.getDefaultMessage(), "")));
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
