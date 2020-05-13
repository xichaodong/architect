package com.chloe.service.impl;

import com.chloe.common.enums.BooleanEnum;
import com.chloe.common.enums.OrderStatusEnum;
import com.chloe.common.org.n3r.idworker.Sid;
import com.chloe.mapper.OrderItemsMapper;
import com.chloe.mapper.OrderStatusMapper;
import com.chloe.mapper.OrdersMapper;
import com.chloe.model.bo.SubmitOrderBO;
import com.chloe.model.pojo.*;
import com.chloe.service.AddressService;
import com.chloe.service.ItemService;
import com.chloe.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Integer DEFAULT_POST_AMOUNT = 0;
    private static final Integer DEFAULT_BUY_COUNT = 1;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private OrderItemsMapper orderItemsMapper;
    @Resource
    private OrderStatusMapper orderStatusMapper;
    @Resource
    private AddressService addressService;
    @Resource
    private ItemService itemService;
    @Resource
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String createOrder(SubmitOrderBO submitOrderBO) {
        Orders newOrder = buildNewOrder(submitOrderBO);

        List<String> specIds = Arrays.asList(submitOrderBO.getItemSpecIds().split(","));

        AtomicInteger normalAmount = new AtomicInteger(0);
        AtomicInteger realPayAmount = new AtomicInteger(0);

        specIds.forEach(specId -> {
            ItemsSpec itemsSpec = itemService.queryItemSpecBySpecId(specId);

            OrderItems orderItems = buildSubOrder(itemsSpec, newOrder.getId());
            orderItemsMapper.insert(orderItems);

            itemService.decreaseItemSpecStock(specId, DEFAULT_BUY_COUNT);

            normalAmount.addAndGet(itemsSpec.getPriceNormal() * DEFAULT_BUY_COUNT);
            realPayAmount.addAndGet(itemsSpec.getPriceDiscount() * DEFAULT_BUY_COUNT);
        });

        newOrder.setTotalAmount(normalAmount.get());
        newOrder.setRealPayAmount(realPayAmount.get());

        ordersMapper.insert(newOrder);

        OrderStatus waitPayOrderStatus = buildOrderStatus(newOrder.getId());
        orderStatusMapper.insert(waitPayOrderStatus);

        return newOrder.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus newStatus = new OrderStatus();
        newStatus.setOrderId(orderId);
        newStatus.setOrderStatus(orderStatus);
        newStatus.setPayTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(newStatus);
    }


    private OrderStatus buildOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();

        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatus.setCreatedTime(new Date());

        return orderStatus;
    }

    private OrderItems buildSubOrder(ItemsSpec itemsSpec, String orderId) {
        Items item = itemService.queryItemById(itemsSpec.getItemId());

        OrderItems orderItems = new OrderItems();

        orderItems.setId(sid.nextShort());
        orderItems.setItemId(itemsSpec.getItemId());
        orderItems.setItemImg(itemService.queryItemMainImgByItemId(itemsSpec.getItemId()));
        orderItems.setItemName(item.getItemName());
        orderItems.setItemSpecName(itemsSpec.getName());
        orderItems.setBuyCounts(DEFAULT_BUY_COUNT);
        orderItems.setItemSpecId(itemsSpec.getId());
        orderItems.setOrderId(orderId);
        orderItems.setPrice(itemsSpec.getPriceDiscount());

        return orderItems;
    }

    private Orders buildNewOrder(SubmitOrderBO submitOrderBO) {
        UserAddress address = addressService.queryAddressById(submitOrderBO.getUserId(), submitOrderBO.getAddressId());

        Orders newOrder = new Orders();

        newOrder.setId(sid.nextShort());
        newOrder.setUserId(submitOrderBO.getUserId());
        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince() + " " + address.getCity() + " " +
                address.getDistrict() + " " + address.getDetail());
        newOrder.setPostAmount(DEFAULT_POST_AMOUNT);
        newOrder.setPayMethod(submitOrderBO.getPayMethod());
        newOrder.setLeftMsg(submitOrderBO.getLeftMsg());
        newOrder.setIsComment(BooleanEnum.FALSE.type);
        newOrder.setIsDelete(BooleanEnum.FALSE.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        return newOrder;
    }
}
