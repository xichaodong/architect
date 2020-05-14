package com.chloe.controller.center;

import com.chloe.common.utils.CookieUtils;
import com.chloe.common.utils.JsonResult;
import com.chloe.common.utils.JsonUtils;
import com.chloe.model.bo.center.CenterUserBO;
import com.chloe.model.pojo.Users;
import com.chloe.service.center.CenterUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("userInfo")
public class CenterUserController {
    private static final String USER_COOKIE_NAME = "user";

    @Resource
    private CenterUserService centerUserService;

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

        CookieUtils.setCookie(request, response, USER_COOKIE_NAME, JsonUtils.objectToJson(users), true);

        return JsonResult.ok();
    }

    @ApiOperation(value = "用户头像上传", notes = "用户头像上传", httpMethod = "POST")
    @PostMapping("update")
    public JsonResult updateUserFace(@RequestParam String userId, MultipartFile multipartFile) {
        if (bindingResult.hasErrors()) {
            return JsonResult.errorMap(getErrors(bindingResult));
        }

        Users users = centerUserService.updateUserInfo(userId, centerUserBO);

        CookieUtils.setCookie(request, response, USER_COOKIE_NAME, JsonUtils.objectToJson(users), true);

        return JsonResult.ok();
    }

    Map<String, String> getErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> StringUtils.defaultIfBlank(fieldError.getDefaultMessage(), "")));
    }
}
