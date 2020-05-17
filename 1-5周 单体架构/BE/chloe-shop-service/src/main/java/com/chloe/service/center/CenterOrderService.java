package com.chloe.service.center;

import com.chloe.common.utils.PagedGridResult;
import com.chloe.model.pojo.Orders;
import com.chloe.model.vo.center.CenterOrderStatusCountsVO;
import sun.jvm.hotspot.debugger.Page;

public interface CenterOrderService {
    PagedGridResult queryCenterUserOrder(String userId, Integer orderStatus, Integer page, Integer pageSize);

    boolean updateDeliverOrderStatus(String orderId);

    boolean updateConfirmDeliverOrderStatus(String orderId);

    boolean deleteOrder(String userId, String orderId);

    Orders queryUserOrder(String userId, String orderId);

    CenterOrderStatusCountsVO getOrderStatusCounts(String userId);

    PagedGridResult getUserOrderTrend(String userId, Integer page,Integer pageSize);
}
