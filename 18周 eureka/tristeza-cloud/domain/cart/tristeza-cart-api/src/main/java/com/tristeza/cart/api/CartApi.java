package com.tristeza.cart.api;

import com.tristeza.cart.model.bo.CartBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author chaodong.xi
 * @date 2021/2/22 10:43 上午
 */
@RequestMapping("cart-api")
@FeignClient("cart-api")
public interface CartApi {
    @PutMapping("sync")
    String syncShopCartData(@RequestParam("userId") String userId, @RequestParam("shopCartCookie") String shopCartCookie);
}
