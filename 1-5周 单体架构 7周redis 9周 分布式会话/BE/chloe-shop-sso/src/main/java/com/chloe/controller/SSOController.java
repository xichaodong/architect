package com.chloe.controller;

import com.chloe.common.utils.*;
import com.chloe.model.bo.UserBO;
import com.chloe.model.pojo.Users;
import com.chloe.model.vo.UserVO;
import com.chloe.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
public class SSOController {
    private static final String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";
    private static final String REDIS_USER_TICKET = "REDIS_USER_TICKET";
    private static final String COOKIE_USER_TICKET = "COOKIE_USER_TICKET";
    private static final String REDIS_TMP_TICKET = "REDIS_TMP_TICKET";

    @Resource
    private UserService userService;
    @Resource
    private RedisOperator redisOperator;

    @GetMapping("login")
    public String login(Model model, String returnUrl, HttpServletRequest request,
                        HttpServletResponse response) {
        model.addAttribute("returnUrl", returnUrl);

        String ticket = getCookie(request, COOKIE_USER_TICKET);

        boolean isVerified = verifyTicket(ticket);

        if (isVerified) {
            return "redirect:" + returnUrl + "?tmpTicket=" + createTmpTicket();
        }

        return "login";
    }

    @PostMapping("logout")
    @ResponseBody
    public JsonResult logout(String userId, HttpServletRequest request,
                             HttpServletResponse response) {
        String ticket = getCookie(request, COOKIE_USER_TICKET);

        delCookie(response, COOKIE_USER_TICKET);

        redisOperator.del(String.format("%s:%s", REDIS_USER_TICKET, ticket));
        redisOperator.del(String.format("%s:%s", REDIS_USER_TOKEN, userId));

        return JsonResult.ok();
    }

    private boolean verifyTicket(String ticket) {
        if (StringUtils.isBlank(ticket)) {
            return false;
        }

        String userId = redisOperator.get(String.format("%s:%s", REDIS_USER_TICKET, ticket));

        if (StringUtils.isBlank(userId)) {
            return false;
        }

        String userSession = redisOperator.get(String.format("%s:%s", REDIS_USER_TOKEN, userId));

        return !StringUtils.isBlank(userSession);
    }

    @PostMapping("verifyTmpTicket")
    @ResponseBody
    public JsonResult verifyTmpTicket(String tmpTicket, HttpServletRequest request,
                                      HttpServletResponse response) {

        String tmpTicketValue = redisOperator.get(String.format("%s:%s", REDIS_TMP_TICKET, tmpTicket));

        if (StringUtils.isBlank(tmpTicketValue)) {
            return JsonResult.errorMsg("用户票据异常");
        }

        try {
            if (!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))) {
                return JsonResult.errorMsg("用户票据异常");
            } else {
                redisOperator.del(String.format("%s:%s", REDIS_TMP_TICKET, tmpTicket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String ticket = getCookie(request, COOKIE_USER_TICKET);
        String userId = redisOperator.get(String.format("%s:%s", REDIS_USER_TICKET, ticket));

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户票据异常");
        }

        String userSession = redisOperator.get(String.format("%s:%s", REDIS_USER_TOKEN, userId));

        if (StringUtils.isBlank(userSession)) {
            return JsonResult.errorMsg("用户票据异常");
        }

        return JsonResult.ok(JsonUtils.jsonToPojo(userSession, UserVO.class));
    }

    @PostMapping("doLogin")
    public String login(Model model, String username, String password,
                        String returnUrl, HttpServletRequest request,
                        HttpServletResponse response) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            model.addAttribute("errmsg", "用户名或密码不能为空");
            return "login";
        }

        UserBO userBo = new UserBO();
        userBo.setUsername(username);
        userBo.setPassword(password);

        Users users = userService.queryUserForLogin(userBo);

        if (Objects.isNull(users)) {
            model.addAttribute("errmsg", "用户名或密码不正确");
            return "login";
        }

        buildUserVO(users, response);

        String tmpTicket = createTmpTicket();

        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
    }

    private void buildUserVO(Users user, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        String ticket = UUID.randomUUID().toString();
        UserVO userVO = new UserVO();

        BeanUtils.copyProperties(user, userVO);
        userVO.setUserUniqueToken(token);

        redisOperator.set(String.format("%s:%s", REDIS_USER_TOKEN, user.getId()), JsonUtils.objectToJson(userVO));
        redisOperator.set(String.format("%s:%s", REDIS_USER_TICKET, ticket), user.getId());

        setCookie(response, COOKIE_USER_TICKET, ticket);
    }

    private String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookie = request.getCookies();
        if (Objects.isNull(cookie) || StringUtils.isBlank(key)) {
            return null;
        }
        Optional<Cookie> cookieValue = Arrays.stream(cookie)
                .filter(x -> x.getName().equals(key)).findFirst();

        return cookieValue.isPresent() ? cookieValue.get().getValue() : "";
    }

    private String createTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString();

        try {
            redisOperator.set(String.format("%s:%s", REDIS_TMP_TICKET, tmpTicket), MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmpTicket;
    }

    private void setCookie(HttpServletResponse response, String key, String val) {
        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void delCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }
}
