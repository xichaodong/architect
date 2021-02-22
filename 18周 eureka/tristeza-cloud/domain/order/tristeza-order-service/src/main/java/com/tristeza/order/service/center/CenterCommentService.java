package com.tristeza.order.service.center;

import com.tristeza.order.model.bo.center.CenterCommentBO;
import com.tristeza.order.model.pojo.OrderItems;

import java.util.List;

public interface CenterCommentService {
    List<OrderItems> queryPendingComments(String orderId);

    void saveComments(String userId, String orderId, List<CenterCommentBO> comments);
}
