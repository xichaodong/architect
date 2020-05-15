package com.chloe.service.impl.center;

import com.chloe.common.enums.BooleanEnum;
import com.chloe.common.org.n3r.idworker.Sid;
import com.chloe.common.utils.PagedGridResult;
import com.chloe.mapper.ItemsCommentsMapper;
import com.chloe.mapper.OrderItemsMapper;
import com.chloe.mapper.OrderStatusMapper;
import com.chloe.mapper.OrdersMapper;
import com.chloe.model.bo.center.CenterCommentBO;
import com.chloe.model.pojo.OrderItems;
import com.chloe.model.pojo.OrderStatus;
import com.chloe.model.pojo.Orders;
import com.chloe.model.vo.center.CenterCommentVO;
import com.chloe.service.center.CenterCommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    private ItemsCommentsMapper itemsCommentsMapper;
    @Resource
    private OrdersMapper ordersMapper;

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

        itemsCommentsMapper.saveComment(userId, comments);

        changeOrderCommentStatus(orderId);

        changeOrderStatusCommentTime(orderId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryUserComment(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<CenterCommentVO> comments = itemsCommentsMapper.queryUserComments(userId);
        PageInfo<CenterCommentVO> pageInfo = new PageInfo<>(comments);

        PagedGridResult result = new PagedGridResult();
        result.setRows(pageInfo.getList());
        result.setTotal(pageInfo.getPages());
        result.setPage(page);
        result.setRecords(pageInfo.getTotal());

        return result;
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
