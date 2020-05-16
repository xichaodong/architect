package com.chloe.service.impl.center;

import com.chloe.common.enums.BooleanEnum;
import com.chloe.common.enums.OrderStatusEnum;
import com.chloe.common.utils.PagedGridResult;
import com.chloe.mapper.OrderStatusMapper;
import com.chloe.mapper.OrdersMapper;
import com.chloe.model.pojo.OrderStatus;
import com.chloe.model.pojo.Orders;
import com.chloe.model.vo.center.CenterOrderStatusCountsVO;
import com.chloe.model.vo.center.CenterOrderTrendVO;
import com.chloe.model.vo.center.CenterOrderVO;
import com.chloe.service.center.CenterOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CenterOrderServiceImpl implements CenterOrderService {
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryCenterUserOrder(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<CenterOrderVO> centerOrderVOS = ordersMapper.queryUserOrder(userId, orderStatus);

        PageInfo<CenterOrderVO> pageInfo = new PageInfo<>(centerOrderVOS);

        return buildPageResult(pageInfo, page);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateDeliverOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();

        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        orderStatus.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);

        return orderStatusMapper.updateByExampleSelective(orderStatus, example) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateConfirmDeliverOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);

        return orderStatusMapper.updateByExampleSelective(orderStatus, example) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setIsDelete(BooleanEnum.TRUE.type);
        orders.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("id", orderId);
        criteria.andEqualTo("userId", userId);

        return ordersMapper.updateByExampleSelective(orders, example) > 0;
    }

    @Override
    public Orders queryUserOrder(String userId, String orderId) {
        Orders orders = new Orders();

        orders.setId(orderId);
        orders.setUserId(userId);

        return ordersMapper.selectOne(orders);
    }

    @Override
    public CenterOrderStatusCountsVO getOrderStatusCounts(String userId) {
        CenterOrderStatusCountsVO countsVO = new CenterOrderStatusCountsVO();

        countsVO.setWaitPayCounts(ordersMapper.getOrderStatusCount(OrderStatusEnum.WAIT_PAY.type, userId, null));
        countsVO.setWaitDeliverCounts(ordersMapper.getOrderStatusCount(OrderStatusEnum.WAIT_DELIVER.type, userId, null));
        countsVO.setWaitReceiveCounts(ordersMapper.getOrderStatusCount(OrderStatusEnum.WAIT_RECEIVE.type, userId, null));
        countsVO.setWaitCommentCounts(ordersMapper.getOrderStatusCount(OrderStatusEnum.SUCCESS.type, userId, BooleanEnum.FALSE.type));

        return countsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getUserOrderTrend(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<CenterOrderTrendVO> userOrderTrend = ordersMapper.getUserOrderTrend(userId);

        PageInfo<CenterOrderTrendVO> pageInfo = new PageInfo<>(userOrderTrend);

        return buildPageResult(pageInfo, page);
    }

    private PagedGridResult buildPageResult(PageInfo<?> pageInfo, Integer page) {
        PagedGridResult result = new PagedGridResult();

        result.setRows(pageInfo.getList());
        result.setPage(page);
        result.setRecords(pageInfo.getTotal());
        result.setTotal(pageInfo.getPages());

        return result;
    }
}
