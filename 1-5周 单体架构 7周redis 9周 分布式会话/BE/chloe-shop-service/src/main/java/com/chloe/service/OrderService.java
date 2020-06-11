package com.chloe.service;

import com.chloe.model.bo.CartBO;
import com.chloe.model.bo.SubmitOrderBO;
import com.chloe.model.pojo.OrderStatus;
import com.chloe.model.vo.CreateOrderVO;

import java.util.List;

public interface OrderService {
    CreateOrderVO createOrder(SubmitOrderBO submitOrderBO, List<CartBO> cartBOS);

    void updateOrderStatus(String orderId, Integer orderStatus);

    OrderStatus queryOrderStatus(String orderId);

    void closeTimeoutOrder();
}
