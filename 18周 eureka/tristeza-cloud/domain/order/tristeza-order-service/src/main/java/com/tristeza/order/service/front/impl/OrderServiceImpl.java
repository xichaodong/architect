package com.tristeza.order.service.front.impl;

import com.tristeza.cart.model.bo.CartBO;
import com.tristeza.cloud.common.idworker.Sid;
import com.tristeza.cloud.common.utils.DateUtil;
import com.tristeza.cloud.model.enums.BooleanEnum;
import com.tristeza.cloud.model.enums.OrderStatusEnum;
import com.tristeza.item.api.ItemApi;
import com.tristeza.item.model.pojo.Items;
import com.tristeza.item.model.pojo.ItemsSpec;
import com.tristeza.order.mapper.OrderItemsMapper;
import com.tristeza.order.mapper.OrderStatusMapper;
import com.tristeza.order.mapper.OrdersMapper;
import com.tristeza.order.model.bo.front.PlaceOrderBO;
import com.tristeza.order.model.bo.front.SubmitOrderBO;
import com.tristeza.order.model.pojo.OrderItems;
import com.tristeza.order.model.pojo.OrderStatus;
import com.tristeza.order.model.pojo.Orders;
import com.tristeza.order.model.vo.front.CreateOrderVO;
import com.tristeza.order.model.vo.front.MerchantOrdersVO;
import com.tristeza.order.service.front.OrderService;
import com.tristeza.user.api.front.AddressApi;
import com.tristeza.user.model.pojo.UserAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    private AddressApi addressApi;
    @Resource
    private ItemApi itemApi;
    @Resource
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CreateOrderVO createOrder(PlaceOrderBO placeOrderBO) {
        Orders newOrder = buildNewOrder(placeOrderBO.getOrderBO());

        List<String> specIds = Arrays.asList(placeOrderBO.getOrderBO().getItemSpecIds().split(","));

        AtomicInteger normalAmount = new AtomicInteger(0);
        AtomicInteger realPayAmount = new AtomicInteger(0);

        List<CartBO> needRemoveBOS = new ArrayList<>();

        specIds.forEach(specId -> {
            ItemsSpec itemsSpec = itemApi.queryItemSpecBySpecId(specId);

            placeOrderBO.getCartBOS().stream().filter(bo -> bo.getSpecId()
                    .equals(specId))
                    .findFirst()
                    .ifPresent(bo -> {
                        needRemoveBOS.add(bo);

                        OrderItems orderItems = buildSubOrder(itemsSpec, newOrder.getId(), bo.getBuyCounts());
                        orderItemsMapper.insert(orderItems);

                        itemApi.decreaseItemSpecStock(specId, bo.getBuyCounts());

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
        Items item = itemApi.queryItemById(itemsSpec.getItemId());

        OrderItems orderItems = new OrderItems();

        orderItems.setId(sid.nextShort());
        orderItems.setItemId(itemsSpec.getItemId());
        orderItems.setItemImg(itemApi.queryItemMainImgByItemId(itemsSpec.getItemId()));
        orderItems.setItemName(item.getItemName());
        orderItems.setItemSpecName(itemsSpec.getName());
        orderItems.setBuyCounts(buyCounts);
        orderItems.setItemSpecId(itemsSpec.getId());
        orderItems.setOrderId(orderId);
        orderItems.setPrice(itemsSpec.getPriceDiscount());

        return orderItems;
    }

    private Orders buildNewOrder(SubmitOrderBO submitOrderBO) {
        UserAddress address = addressApi.queryAddressById(submitOrderBO.getUserId(), submitOrderBO.getAddressId());

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
