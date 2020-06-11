package com.chloe.controller;

import com.chloe.common.utils.CookieUtils;
import com.chloe.common.utils.JsonResult;
import com.chloe.common.utils.JsonUtils;
import com.chloe.common.utils.RedisOperator;
import com.chloe.model.bo.CartBO;
import com.chloe.model.bo.UserBo;
import com.chloe.model.pojo.Users;
import com.chloe.model.vo.UserVO;
import com.chloe.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Api(value = "注册登录", tags = {"用于注册登录的接口"})
@RestController
@RequestMapping("passport")
public class PassportController {
    private static final String SHOP_CART_CACHE_KEY = "SHOP_CART_CACHE_KEY";
    private static final String SHOP_CART_NAME = "shopcart";
    private static final String USER_INFO_COOKIE_NAME = "user";
    private static final String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";

    @Resource
    private UserService userService;
    @Resource
    private RedisOperator redisOperator;

    @GetMapping("usernameIsExist")
    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    public JsonResult usernameIsExist(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            return JsonResult.errorMsg("用户名不能为空");
        }

        boolean isExist = userService.queryUsernameIsExist(username);

        return isExist ? JsonResult.errorMsg("用户名已存在") : JsonResult.ok();
    }

    @PostMapping("register")
    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    public JsonResult register(@RequestBody UserBo userBo, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userBo.getUsername()) || StringUtils.isBlank(userBo.getPassword())
                || StringUtils.isBlank(userBo.getConfirmPassword())) {
            return JsonResult.errorMsg("用户名或密码不能为空");
        }

        if (userBo.getPassword().length() < 6) {
            return JsonResult.errorMsg("密码长度不能小于6");
        }

        if (!userBo.getPassword().equals(userBo.getConfirmPassword())) {
            return JsonResult.errorMsg("两次输入的密码不一致");
        }

        boolean isExist = userService.queryUsernameIsExist(userBo.getUsername());

        if (isExist) {
            return JsonResult.errorMsg("用户名已存在");
        }

        Users user = userService.createUser(userBo);

        UserVO userVO = buildUserVO(user);

        CookieUtils.setCookie(request, response, USER_INFO_COOKIE_NAME, JsonUtils.objectToJson(userVO), true);

        return JsonResult.ok(userVO);
    }

    @PostMapping("login")
    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    public JsonResult login(@RequestBody UserBo userBo, HttpServletRequest request,
                            HttpServletResponse response) {
        if (StringUtils.isBlank(userBo.getUsername()) || StringUtils.isBlank(userBo.getPassword())) {
            return JsonResult.errorMsg("用户名或密码不能为空");
        }

        Users users = userService.queryUserForLogin(userBo);

        if (Objects.isNull(users)) {
            return JsonResult.errorMsg("用户名或密码不正确");
        }

        UserVO userVO = buildUserVO(users);

        CookieUtils.setCookie(request, response, USER_INFO_COOKIE_NAME, JsonUtils.objectToJson(userVO), true);

        syncShopCartData(request, response, users.getId());

        return JsonResult.ok(userVO);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("logout")
    public JsonResult logout(HttpServletRequest request, HttpServletResponse response, @RequestParam String userId) {
        CookieUtils.deleteCookie(request, response, USER_INFO_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, SHOP_CART_NAME);

        redisOperator.del(String.format("%s:%s", REDIS_USER_TOKEN, userId));

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

    private void syncShopCartData(HttpServletRequest request, HttpServletResponse response, String userId) {
        String shopCacheKey = String.format("%s:%s", SHOP_CART_CACHE_KEY, userId);
        String shopCartCache = redisOperator.get(shopCacheKey);

        String shopCartCookie = CookieUtils.getCookieValue(request, SHOP_CART_NAME, true);

        if (StringUtils.isBlank(shopCartCache)) {
            if (StringUtils.isNotBlank(shopCartCookie)) {
                redisOperator.set(shopCacheKey, shopCartCookie);
            }
        } else {
            if (StringUtils.isNotBlank(shopCartCookie)) {
                List<CartBO> cacheBOS = JsonUtils.jsonToList(shopCartCache, CartBO.class);
                List<CartBO> cookieBOS = JsonUtils.jsonToList(shopCartCookie, CartBO.class);

                if (!CollectionUtils.isEmpty(cookieBOS) && !CollectionUtils.isEmpty(cacheBOS)) {
                    Map<String, CartBO> cookieBuyCountMapping = cookieBOS.stream()
                            .collect(Collectors.toMap(CartBO::getSpecId, Function.identity()));


                    List<CartBO> needDeleteBOS = new ArrayList<>();

                    cacheBOS.stream().filter(bo -> Objects.nonNull(cookieBuyCountMapping.get(bo.getSpecId())))
                            .forEach(bo -> {
                                bo.setBuyCounts(cookieBuyCountMapping.get(bo.getSpecId()).getBuyCounts());
                                needDeleteBOS.add(cookieBuyCountMapping.get(bo.getSpecId()));
                            });

                    cookieBOS.removeAll(needDeleteBOS);
                    cacheBOS.addAll(cookieBOS);

                    CookieUtils.setCookie(request, response, SHOP_CART_NAME, JsonUtils.objectToJson(cacheBOS), true);
                    redisOperator.set(shopCacheKey, JsonUtils.objectToJson(cacheBOS));
                }
            } else {
                CookieUtils.setCookie(request, response, SHOP_CART_NAME, shopCartCache, true);
            }
        }
    }
}
