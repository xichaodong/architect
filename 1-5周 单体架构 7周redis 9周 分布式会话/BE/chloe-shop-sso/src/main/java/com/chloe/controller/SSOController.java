package com.chloe.controller;

import com.chloe.common.utils.CookieUtils;
import com.chloe.common.utils.JsonResult;
import com.chloe.common.utils.JsonUtils;
import com.chloe.common.utils.RedisOperator;
import com.chloe.model.bo.UserBO;
import com.chloe.model.pojo.Users;
import com.chloe.model.vo.UserVO;
import com.chloe.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

@Controller
public class SSOController {
    private static final String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";

    @Resource
    private UserService userService;
    @Resource
    private RedisOperator redisOperator;

    @GetMapping("login")
    public String login(Model model, String returnUrl, HttpServletRequest request,
                        HttpServletResponse response) {
        model.addAttribute("returnUrl", returnUrl);

        return "login";
    }

    @PostMapping("doLogin")
    public String login(Model model, String username, String password,
                        String returnUrl, HttpServletRequest request,
                        HttpServletResponse response) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            model.addAttribute("errmsg","用户名或密码不能为空");
            return "login";
        }

        UserBO userBo = new UserBO();
        userBo.setUsername(username);
        userBo.setPassword(password);

        Users users = userService.queryUserForLogin(userBo);

        if (Objects.isNull(users)) {
            model.addAttribute("errmsg","用户名或密码不正确");
            return "login";
        }

        UserVO userVO = buildUserVO(users);

//        CookieUtils.setCookie(request, response, USER_INFO_COOKIE_NAME, JsonUtils.objectToJson(userVO), true);

        return "login";
    }

    private UserVO buildUserVO(Users user) {
        String token = UUID.randomUUID().toString();
        UserVO userVO = new UserVO();

        BeanUtils.copyProperties(user, userVO);
        userVO.setUserUniqueToken(token);

        redisOperator.set(String.format("%s:%s", REDIS_USER_TOKEN, user.getId()), JsonUtils.objectToJson(userVO));

        return userVO;
    }
}
