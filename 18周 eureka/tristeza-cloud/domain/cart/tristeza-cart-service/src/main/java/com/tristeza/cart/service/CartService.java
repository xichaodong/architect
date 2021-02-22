package com.tristeza.cart.service;

import com.tristeza.cart.model.bo.CartBO;

/**
 * @author chaodong.xi
 * @date 2021/2/22 10:43 上午
 */
public interface CartService {
    void addItem(String userId, CartBO cartBO);

    void deleteItem(String userId, String itemSpecId);

    String syncShopCartData(String userId, String shopCartCookie);

    void clearCart(String userId);
}
