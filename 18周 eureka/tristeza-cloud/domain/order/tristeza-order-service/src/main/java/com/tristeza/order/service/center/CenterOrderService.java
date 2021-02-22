package com.tristeza.order.service.center;

import com.tristeza.cloud.model.pojo.JsonResult;
import com.tristeza.cloud.model.pojo.PagedGridResult;
import com.tristeza.order.model.pojo.Orders;
import com.tristeza.order.model.vo.center.CenterOrderStatusCountsVO;

public interface CenterOrderService {
    PagedGridResult queryCenterUserOrder(String userId, Integer orderStatus, Integer page, Integer pageSize);

    boolean updateDeliverOrderStatus(String orderId);

    boolean updateConfirmDeliverOrderStatus(String orderId);

    boolean deleteOrder(String userId, String orderId);

    Orders queryUserOrder(String userId, String orderId);

    CenterOrderStatusCountsVO getOrderStatusCounts(String userId);

    PagedGridResult getUserOrderTrend(String userId, Integer page, Integer pageSize);

    Orders checkUserOrder(String userId, String orderId);
}
