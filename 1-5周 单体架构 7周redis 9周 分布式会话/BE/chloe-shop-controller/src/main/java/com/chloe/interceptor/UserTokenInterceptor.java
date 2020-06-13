package com.chloe.interceptor;

import com.chloe.common.utils.JsonResult;
import com.chloe.common.utils.JsonUtils;
import com.chloe.common.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class UserTokenInterceptor implements HandlerInterceptor {
    private static final String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";
    private static final String HEADER_TOKEN_ID = "headerUserId";
    private static final String HEADER_TOKEN = "headerUserToken";

    @Resource
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HEADER_TOKEN);
        String userId = request.getHeader(HEADER_TOKEN_ID);

        if (StringUtils.isNotBlank(token) && StringUtils.isNotBlank(userId)) {
            String tokenCache = redisOperator.get(String.format("%s:%s", REDIS_USER_TOKEN, userId));
            if (StringUtils.isBlank(tokenCache)) {
                returnErrorResponse(response, JsonResult.errorMsg("请登录..."));
                return false;
            } else if (!tokenCache.equals(token)) {
                returnErrorResponse(response, JsonResult.errorMsg("账号在异地登录..."));
                return false;
            }
        } else {
            returnErrorResponse(response, JsonResult.errorMsg("请登录..."));
            return false;
        }

        return true;
    }

    public void returnErrorResponse(HttpServletResponse response, JsonResult jsonResult) {
        OutputStream out = null;

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");

        try {
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(jsonResult).getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
