package com.chloe.service.impl;

import com.chloe.common.enums.BooleanEnum;
import com.chloe.common.enums.OrderStatusEnum;
import com.chloe.common.org.n3r.idworker.Sid;
import com.chloe.common.utils.DateUtil;
import com.chloe.mapper.OrderItemsMapper;
import com.chloe.mapper.OrderStatusMapper;
import com.chloe.mapper.OrdersMapper;
import com.chloe.model.bo.CartBO;
import com.chloe.model.bo.SubmitOrderBO;
import com.chloe.model.pojo.*;
import com.chloe.model.vo.CreateOrderVO;
import com.chloe.model.vo.MerchantOrdersVO;
import com.chloe.service.AddressService;
import com.chloe.service.ItemService;
import com.chloe.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Integer DEFAULT_POST_AMOUNT = 0;
    private static final String CALLBACK_URL = "http://www.chloebang.com/orders/notifyMerchantOrderPaid";
    private static final Integer TEST_AMOUNT = 1;

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
    public CreateOrderVO createOrder(SubmitOrderBO submitOrderBO, List<CartBO> cartBOS) {
        Orders newOrder = buildNewOrder(submitOrderBO);

        List<String> specIds = Arrays.asList(submitOrderBO.getItemSpecIds().split(","));

        AtomicInteger normalAmount = new AtomicInteger(0);
        AtomicInteger realPayAmount = new AtomicInteger(0);

        List<CartBO> needRemoveBOS = new ArrayList<>();

        specIds.forEach(specId -> {
            ItemsSpec itemsSpec = itemService.queryItemSpecBySpecId(specId);

            cartBOS.stream().filter(bo -> bo.getSpecId()
                    .equals(specId))
                    .findFirst()
                    .ifPresent(bo -> {
                        needRemoveBOS.add(bo);

                        OrderItems orderItems = buildSubOrder(itemsSpec, newOrder.getId(), bo.getBuyCounts());
                        orderItemsMapper.insert(orderItems);

                        itemService.decreaseItemSpecStock(specId, bo.getBuyCounts());

                        normalAmount.addAndGet(itemsSpec.getPriceNormal() * bo.getBuyCounts());
                        realPayAmount.addAndGet(itemsSpec.getPriceDiscount() * bo.getBuyCounts());
                    });
        });

        newOrder.setTotalAmount(normalAmount.get());
        newOrder.setRealPayAmount(realPayAmount.get());

        ordersMapper.insert(newOrder);

        OrderStatus waitPayOrderStatus = buildOrderStatus(newOrder.getId());
        orderStatusMapper.insert(waitPayOrderStatus);

        return buildCreateOrderVO(newOrder, needRemoveBOS);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus newStatus = new OrderStatus();
        newStatus.setOrderId(orderId);
        newStatus.setOrderStatus(orderStatus);
        newStatus.setPayTime(new Date());

        System.out.println(System.currentTimeMillis());
        orderStatusMapper.updateByPrimaryKeySelective(newStatus);
        System.out.println(System.currentTimeMillis());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatus queryOrderStatus(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void closeTimeoutOrder() {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);

        List<OrderStatus> orderStatuses = orderStatusMapper.select(orderStatus);

        Date now = new Date();

        orderStatuses.forEach(status -> {
            int diff = DateUtil.daysBetween(status.getCreatedTime(), now);

            if (diff >= 1) {
                doClose(status.getOrderId());
            }
        });
    }

    private void doClose(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatus.setCloseTime(new Date());
        orderStatus.setOrderId(orderId);

        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    private CreateOrderVO buildCreateOrderVO(Orders newOrder, List<CartBO> cartBOS) {
        MerchantOrdersVO merchantOrdersVO = buildMerchantOrdersVO(newOrder);

        CreateOrderVO createOrderVO = new CreateOrderVO();
        createOrderVO.setOrderId(newOrder.getId());
        createOrderVO.setMerchantOrdersVO(merchantOrdersVO);
        createOrderVO.setNeedRemoveBOS(cartBOS);

        return createOrderVO;
    }

    private MerchantOrdersVO buildMerchantOrdersVO(Orders newOrder) {
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();

        merchantOrdersVO.setMerchantOrderId(newOrder.getId());
//        merchantOrdersVO.setAmount(newOrder.getPostAmount() + newOrder.getRealPayAmount());
        merchantOrdersVO.setAmount(TEST_AMOUNT);
        merchantOrdersVO.setPayMethod(newOrder.getPayMethod());
        merchantOrdersVO.setMerchantUserId(newOrder.getUserId());
        merchantOrdersVO.setReturnUrl(CALLBACK_URL);

        return merchantOrdersVO;
    }

    private OrderStatus buildOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();

        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatus.setCreatedTime(new Date());

        return orderStatus;
    }

    private OrderItems buildSubOrder(ItemsSpec itemsSpec, String orderId, Integer buyCounts) {
        Items item = itemService.queryItemById(itemsSpec.getItemId());

        OrderItems orderItems = new OrderItems();

        orderItems.setId(sid.nextShort());
        orderItems.setItemId(itemsSpec.getItemId());
        orderItems.setItemImg(itemService.queryItemMainImgByItemId(itemsSpec.getItemId()));
        orderItems.setItemName(item.getItemName());
        orderItems.setItemSpecName(itemsSpec.getName());
        orderItems.setBuyCounts(buyCounts);
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
