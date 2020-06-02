package com.chloe.controller;

import com.chloe.common.utils.JsonResult;
import com.chloe.common.utils.JsonUtils;
import com.chloe.common.utils.RedisOperator;
import com.chloe.model.bo.CartBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Api(value = "购物车相关操作", tags = {"购物车相关接口"})
@RestController
@RequestMapping("shopCart")
public class CartController {
    private static final String SHOP_CART_CACHE_KEY = "SHOP_CART_CACHE_KEY";

    @Resource
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("add")
    public JsonResult add(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam String userId, @RequestBody CartBO cartBO) {

        String cacheKey = String.format("%s:%s", SHOP_CART_CACHE_KEY, userId);

        String shopCartCache = redisOperator.get(cacheKey);

        List<CartBO> cartBOS = new ArrayList<>();

        if (StringUtils.isNotBlank(shopCartCache)) {
            List<CartBO> oldCartBOS = JsonUtils.jsonToList(shopCartCache, CartBO.class);

            AtomicBoolean atomicBoolean = new AtomicBoolean(false);

            if (!CollectionUtils.isEmpty(oldCartBOS)) {
                oldCartBOS.forEach(bo -> {
                    if (bo.getSpecId().equals(cartBO.getSpecId())) {
                        bo.setBuyCounts(bo.getBuyCounts() + cartBO.getBuyCounts());
                        if (!atomicBoolean.get()) {
                            atomicBoolean.set(true);
                        }
                    }
                });
                cartBOS.addAll(oldCartBOS);
            }

            if (!atomicBoolean.get()) {
                cartBOS.add(cartBO);
            }
        } else {
            cartBOS.add(cartBO);
        }

        redisOperator.set(cacheKey, JsonUtils.objectToJson(cartBOS));

        return JsonResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("del")
    public JsonResult add(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam String userId, @RequestParam String itemSpecId) {
        if (StringUtils.isBlank(itemSpecId)) {
            return JsonResult.errorMsg("商品规格id不能为空");
        }
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        String cacheKey = String.format("%s:%s", SHOP_CART_CACHE_KEY, userId);

        String shopCartCache = redisOperator.get(cacheKey);

        if (StringUtils.isNotBlank(shopCartCache)) {
            List<CartBO> oldCartBOS = JsonUtils.jsonToList(shopCartCache, CartBO.class);

            if (!CollectionUtils.isEmpty(oldCartBOS)) {
                oldCartBOS.stream()
                        .filter(bo -> bo.getSpecId().equals(itemSpecId))
                        .findFirst().ifPresent(oldCartBOS::remove);
            }
            if (CollectionUtils.isEmpty(oldCartBOS)) {
                redisOperator.del(cacheKey);
            } else {
                redisOperator.set(cacheKey, JsonUtils.objectToJson(oldCartBOS));
            }
        }

        return JsonResult.ok();
    }
}
