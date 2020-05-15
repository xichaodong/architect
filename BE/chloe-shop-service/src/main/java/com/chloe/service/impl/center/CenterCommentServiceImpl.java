package com.chloe.service.impl.center;

import com.chloe.mapper.OrderItemsMapper;
import com.chloe.model.pojo.OrderItems;
import com.chloe.service.center.CenterCommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CenterCommentServiceImpl implements CenterCommentService {
    @Resource
    private OrderItemsMapper orderItemsMapper;

    @Override
    public List<OrderItems> queryPendingComments(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);

        return orderItemsMapper.select(orderItems);
    }
}
