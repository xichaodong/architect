package com.tristeza.cart.api;

import com.tristeza.cart.model.bo.CartBO;
import org.springframework.web.bind.annotation.*;

/**
 * @author chaodong.xi
 * @date 2021/2/22 10:43 上午
 */
@RequestMapping("cart-api")
public interface CartApi {
    @PostMapping("item")
    void addItem(@RequestParam("userId") String userId, @RequestBody CartBO cartBO);

    @DeleteMapping("item")
    void deleteItem(@RequestParam("userId") String userId, @RequestParam("itemSpecId") String itemSpecId);

    @PutMapping("sync")
    String syncShopCartData(@RequestParam("userId") String userId, @RequestParam("shopCartCookie") String shopCartCookie);

    @PostMapping("clearCart")
    void clearCart(@RequestParam("userId") String userId);
}
