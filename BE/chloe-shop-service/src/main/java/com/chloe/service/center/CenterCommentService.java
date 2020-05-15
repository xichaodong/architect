package com.chloe.service.center;

import com.chloe.model.pojo.OrderItems;

import java.util.List;

public interface CenterCommentService {
    List<OrderItems> queryPendingComments(String orderId);
}
