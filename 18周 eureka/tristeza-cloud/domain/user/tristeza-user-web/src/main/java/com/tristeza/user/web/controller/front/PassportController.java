package com.tristeza.user.web.controller.front;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.tristeza.cart.api.CartApi;
import com.tristeza.cloud.common.utils.CookieUtils;
import com.tristeza.cloud.common.utils.JsonUtils;
import com.tristeza.cloud.common.utils.RedisOperator;
import com.tristeza.cloud.model.pojo.JsonResult;
import com.tristeza.user.model.bo.UserBO;
import com.tristeza.user.model.pojo.Users;
import com.tristeza.user.model.vo.UserVO;
import com.tristeza.user.service.front.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

@Api(value = "注册登录", tags = {"用于注册登录的接口"})
@RestController
@RequestMapping("passport")
public class PassportController {
    private static final String SHOP_CART_NAME = "shopcart";
    private static final String USER_INFO_COOKIE_NAME = "user";
    private static final String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";

    @Resource
    private UserService userService;
    @Resource
    private CartApi cartApi;
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
    public JsonResult register(@RequestBody UserBO userBo, HttpServletRequest request, HttpServletResponse response) {
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
    @HystrixCommand(commandKey = "loginFail", groupKey = "passport", fallbackMethod = "loginFail",
            ignoreExceptions = {IllegalArgumentException.class}, threadPoolKey = "loginThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    //size > 0 -> LinkedBlockingQueue
                    //size = -1(default) -> SynchronousQueue
                    @HystrixProperty(name = "maxQueueSize", value = "40"),
                    //maxQueueSize = -1时无效，使用该参数相当于可以动态设置队列的长度
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                    //线程池统计窗口持续时间
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1024"),
                    //线程池统计窗口内被划分的数量
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "18")
            })
    public JsonResult login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request,
                            HttpServletResponse response) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JsonResult.errorMsg("用户名或密码不能为空");
        }

        Users users = userService.queryUserForLogin(username, password);

        if (Objects.isNull(users)) {
            return JsonResult.errorMsg("用户名或密码不正确");
        }

        UserVO userVO = buildUserVO(users);

        CookieUtils.setCookie(request, response, USER_INFO_COOKIE_NAME, JsonUtils.objectToJson(userVO), true);

//        cartApi.syncShopCartData();

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

    private JsonResult loginFail(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request,
                                 HttpServletResponse response, Throwable throwable) {
        return JsonResult.errorMsg("验证码错误(模仿12306)");
    }
}
