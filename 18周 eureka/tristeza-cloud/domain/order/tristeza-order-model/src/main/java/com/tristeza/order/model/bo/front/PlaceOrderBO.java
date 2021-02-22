package com.tristeza.order.model.bo.front;

import com.tristeza.cart.model.bo.CartBO;

import java.util.List;

/**
 * @author chaodong.xi
 * @date 2021/2/22 2:35 下午
 */
public class PlaceOrderBO {
    private SubmitOrderBO orderBO;

    private List<CartBO> cartBOS;

    public PlaceOrderBO(SubmitOrderBO orderBO, List<CartBO> cartBOS) {
        this.orderBO = orderBO;
        this.cartBOS = cartBOS;
    }

    public SubmitOrderBO getOrderBO() {
        return orderBO;
    }

    public void setOrderBO(SubmitOrderBO orderBO) {
        this.orderBO = orderBO;
    }

    public List<CartBO> getCartBOS() {
        return cartBOS;
    }

    public void setCartBOS(List<CartBO> cartBOS) {
        this.cartBOS = cartBOS;
    }
}
