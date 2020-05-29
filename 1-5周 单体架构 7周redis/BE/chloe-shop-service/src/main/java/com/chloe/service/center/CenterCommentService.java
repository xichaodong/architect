package com.chloe.service.center;

import com.chloe.common.utils.PagedGridResult;
import com.chloe.model.bo.center.CenterCommentBO;
import com.chloe.model.pojo.OrderItems;

import java.util.List;

public interface CenterCommentService {
    List<OrderItems> queryPendingComments(String orderId);

    void saveComments(String userId, String orderId, List<CenterCommentBO> comments);

    PagedGridResult queryUserComment(String userId, Integer page, Integer pageSize);
}
