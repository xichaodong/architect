package com.chloe.service.center;

import com.chloe.common.utils.PagedGridResult;
import com.chloe.model.pojo.Orders;

public interface CenterOrderService {
    PagedGridResult queryCenterUserOrder(String userId, Integer orderStatus, Integer page, Integer pageSize);

    boolean updateDeliverOrderStatus(String orderId);

    boolean updateConfirmDeliverOrderStatus(String orderId);

    boolean deleteOrder(String userId, String orderId);

    Orders queryUserOrder(String userId, String orderId);
}
