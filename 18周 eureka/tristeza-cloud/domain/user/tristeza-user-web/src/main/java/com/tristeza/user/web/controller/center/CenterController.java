package com.tristeza.user.web.controller.center;

import com.tristeza.cloud.model.pojo.JsonResult;
import com.tristeza.user.service.center.CenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "用户中心相关操作", tags = {"用户中心相关接口"})
@RestController
@RequestMapping("center")
public class CenterController {
    @Resource
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "POST")
    @GetMapping("userInfo")
    public JsonResult queryUserInfo(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        return JsonResult.ok(centerUserService.queryUserInfo(userId));
    }
}
