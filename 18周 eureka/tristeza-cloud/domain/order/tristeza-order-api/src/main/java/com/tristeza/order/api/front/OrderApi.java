package com.tristeza.order.api.front;

import com.tristeza.order.model.bo.PlaceOrderBO;
import com.tristeza.order.model.pojo.OrderStatus;
import com.tristeza.order.model.vo.front.CreateOrderVO;
import org.springframework.web.bind.annotation.*;

@RequestMapping("order-api")
public interface OrderApi {
    @PostMapping("order")
    CreateOrderVO createOrder(@RequestBody PlaceOrderBO placeOrderBO);

    @PutMapping("status")
    void updateOrderStatus(@RequestParam("orderId") String orderId,
                           @RequestParam("orderStatus") Integer orderStatus);

    @GetMapping("status")
    OrderStatus queryOrderStatus(@RequestParam("orderId") String orderId);

    @PostMapping("closePendingOrders")
    void closeTimeoutOrder();
}
