package com.tristeza.cart.web.controller;

import com.tristeza.cart.model.bo.CartBO;
import com.tristeza.cart.service.CartService;
import com.tristeza.cloud.common.utils.CookieUtils;
import com.tristeza.cloud.model.pojo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Api(value = "购物车相关操作", tags = {"购物车相关接口"})
@RestController
@RequestMapping("shopCart")
public class CartController {
    private static final String SHOP_CART_NAME = "shopcart";

    @Resource
    private CartService cartService;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("add")
    public JsonResult add(@RequestParam String userId, @RequestBody CartBO cartBO) {
        cartService.addItem(userId, cartBO);
        return JsonResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "DELETE")
    @DeleteMapping("del")
    public JsonResult delete(@RequestParam String userId, @RequestParam String itemSpecId) {
        if (StringUtils.isBlank(itemSpecId)) {
            return JsonResult.errorMsg("商品规格id不能为空");
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        cartService.deleteItem(userId, itemSpecId);
        return JsonResult.ok();
    }

    @ApiOperation(value = "同步购物车", notes = "同步购物车", httpMethod = "PUT")
    @PutMapping("sync")
    public JsonResult sync(HttpServletRequest request, HttpServletResponse response, @RequestParam("userId") String userId) {
        String shopCartCookie = CookieUtils.getCookieValue(request, SHOP_CART_NAME, true);
        String syncCookie = cartService.syncShopCartData(userId, shopCartCookie);

        if (Objects.nonNull(syncCookie)) {
            CookieUtils.setCookie(request, response, SHOP_CART_NAME, syncCookie, true);
        }

        return JsonResult.ok();
    }

    @ApiOperation(value = "清空购物车", notes = "清空购物车", httpMethod = "DELETE")
    @DeleteMapping("clear")
    public JsonResult clearCart(@RequestParam("userId") String userId) {
        cartService.clearCart(userId);
        return JsonResult.ok();
    }
}
