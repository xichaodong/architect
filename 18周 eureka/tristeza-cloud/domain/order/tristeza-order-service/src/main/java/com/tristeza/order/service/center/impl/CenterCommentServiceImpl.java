package com.tristeza.order.service.center.impl;

import com.tristeza.cloud.common.idworker.Sid;
import com.tristeza.cloud.model.enums.BooleanEnum;
import com.tristeza.item.api.ItemCommentsApi;
import com.tristeza.item.model.bo.CenterCommentBO;
import com.tristeza.order.mapper.OrderItemsMapper;
import com.tristeza.order.mapper.OrderStatusMapper;
import com.tristeza.order.mapper.OrdersMapper;
import com.tristeza.order.model.pojo.OrderItems;
import com.tristeza.order.model.pojo.OrderStatus;
import com.tristeza.order.model.pojo.Orders;
import com.tristeza.order.service.center.CenterCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CenterCommentServiceImpl implements CenterCommentService {
    @Resource
    private Sid sid;
    @Resource
    private OrderItemsMapper orderItemsMapper;
    @Resource
    private OrderStatusMapper orderStatusMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private ItemCommentsApi itemCommentsApi;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComments(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);

        return orderItemsMapper.select(orderItems);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String userId, String orderId, List<CenterCommentBO> comments) {
        comments.forEach(comment -> comment.setCommentId(sid.nextShort()));

        itemCommentsApi.saveComments(userId, orderId, comments);

        changeOrderCommentStatus(orderId);

        changeOrderStatusCommentTime(orderId);
    }

    private void changeOrderCommentStatus(String orderId) {
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(BooleanEnum.TRUE.type);

        ordersMapper.updateByPrimaryKeySelective(orders);
    }

    private void changeOrderStatusCommentTime(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}
