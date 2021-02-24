package com.tristeza.order.service.front;

import com.tristeza.order.model.bo.PlaceOrderBO;
import com.tristeza.order.model.pojo.OrderStatus;
import com.tristeza.order.model.vo.front.CreateOrderVO;

public interface OrderService {
    CreateOrderVO createOrder(PlaceOrderBO placeOrderBO);

    void updateOrderStatus(String orderId, Integer orderStatus);

    OrderStatus queryOrderStatus(String orderId);

    void closeTimeoutOrder();
}
