package com.tristeza.order.mapper;

import com.tristeza.cloud.common.my.mapper.MyMapper;
import com.tristeza.order.model.pojo.Orders;
import com.tristeza.order.model.vo.center.CenterOrderTrendVO;
import com.tristeza.order.model.vo.center.CenterOrderVO;

import java.util.List;

public interface OrdersMapper extends MyMapper<Orders> {
    List<CenterOrderVO> queryUserOrder(String userId, Integer orderStatus);

    Integer getOrderStatusCount(Integer orderStatus, String userId, Integer isComment);

    List<CenterOrderTrendVO> getUserOrderTrend(String userId);
}