package com.chloe.controller;

import com.chloe.common.utils.JsonResult;
import com.chloe.model.bo.CartBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车相关操作", tags = {"购物车相关接口"})
@RestController
@RequestMapping("shopCart")
public class CartController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("add")
    public JsonResult add(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam String userId, @RequestBody CartBO cartBO) {

        return JsonResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("del")
    public JsonResult add(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam String userId, @RequestBody String itemSpecId) {
        if (StringUtils.isBlank(itemSpecId)) {
            return JsonResult.errorMsg("商品规格id不能为空");
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        return JsonResult.ok();
    }
}
