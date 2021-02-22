package com.tristeza.cart.service.impl;

import com.tristeza.cart.model.bo.CartBO;
import com.tristeza.cart.service.CartService;
import com.tristeza.cloud.common.utils.JsonUtils;
import com.tristeza.cloud.common.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chaodong.xi
 * @date 2021/2/22 10:51 上午
 */
@Service
public class CartServiceImpl implements CartService {
    private static final String SHOP_CART_CACHE_KEY = "SHOP_CART_CACHE_KEY";

    @Resource
    private RedisOperator redisOperator;

    @Override
    public void addItem(String userId, CartBO cartBO) {

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
    }

    @Override
    public void deleteItem(String userId, String itemSpecId) {
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
    }

    @Override
    public String syncShopCartData(String userId, String shopCartCookie) {
        String shopCacheKey = String.format("%s:%s", SHOP_CART_CACHE_KEY, userId);
        String shopCartCache = redisOperator.get(shopCacheKey);

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

                    redisOperator.set(shopCacheKey, JsonUtils.objectToJson(cacheBOS));

                    return JsonUtils.objectToJson(cacheBOS);
                }
            } else {
                return shopCartCache;
            }
        }

        return null;
    }

    @Override
    public void clearCart(String userId) {
        String cacheKey = String.format("%s:%s", SHOP_CART_CACHE_KEY, userId);
        redisOperator.del(cacheKey);
    }
}
