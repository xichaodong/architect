package com.chloe.service;

import com.chloe.model.bo.SubmitOrderBO;
import com.chloe.model.pojo.OrderStatus;
import com.chloe.model.vo.CreateOrderVO;

public interface OrderService {
    CreateOrderVO createOrder(SubmitOrderBO submitOrderBO);

    void updateOrderStatus(String orderId, Integer orderStatus);

    OrderStatus queryOrderStatus(String orderId);

    void closeTimeoutOrder();
}
