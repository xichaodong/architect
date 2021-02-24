package com.tristeza.cart.remote;

import com.tristeza.cart.api.CartApi;
import com.tristeza.cart.service.CartService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2021/2/24 11:32 上午
 */
@RestController
public class CartApiImpl implements CartApi {
    @Resource
    private CartService cartService;

    @Override
    public String syncShopCartData(String userId, String shopCartCookie) {
        return cartService.syncShopCartData(userId, shopCartCookie);
    }
}
