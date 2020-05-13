package com.chloe.service;

import com.chloe.model.bo.SubmitOrderBO;

public interface OrderService {
    String createOrder(SubmitOrderBO submitOrderBO);

    void updateOrderStatus(String orderId, Integer orderStatus);
}
