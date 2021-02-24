package com.tristeza.order.service.center;

import com.tristeza.item.model.bo.CenterCommentBO;
import com.tristeza.order.model.pojo.OrderItems;

import java.util.List;

public interface CenterCommentService {
    List<OrderItems> queryPendingComments(String orderId);

    void saveComments(String userId, String orderId, List<CenterCommentBO> comments);
}
