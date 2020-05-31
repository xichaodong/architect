package com.chloe.controller;

import com.chloe.common.enums.OrderStatusEnum;
import com.chloe.common.enums.PayMethodEnum;
import com.chloe.common.utils.CookieUtils;
import com.chloe.common.utils.JsonResult;
import com.chloe.common.utils.JsonUtils;
import com.chloe.common.utils.RedisOperator;
import com.chloe.model.bo.CartBO;
import com.chloe.model.bo.SubmitOrderBO;
import com.chloe.model.vo.CreateOrderVO;
import com.chloe.model.vo.MerchantOrdersVO;
import com.chloe.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Api(value = "订单相关操作", tags = {"订单相关接口"})
@RestController
@RequestMapping("orders")
public class OrderController {
    private static final String PAY_CENTER_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    private static final String SHOP_CART_CACHE_KEY = "SHOP_CART_CACHE_KEY";
    private static final String SHOP_CART_NAME = "shopcart";

    @Resource
    private OrderService orderService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户提交订单", notes = "用户提交订单", httpMethod = "POST")
    @PostMapping("create")
    public JsonResult createOrder(@RequestBody SubmitOrderBO submitOrderBO, HttpServletRequest request, HttpServletResponse response) {
        if (!PayMethodEnum.WX.type.equals(submitOrderBO.getPayMethod()) &&
                !PayMethodEnum.ALI_PAY.type.equals(submitOrderBO.getPayMethod())) {
            return JsonResult.errorMsg("支付方式不支持");
        }

        String shopCacheKey = String.format("%s:%s", SHOP_CART_CACHE_KEY, submitOrderBO.getUserId());
        String shopCartCache = redisOperator.get(shopCacheKey);

        if (StringUtils.isBlank(shopCartCache)) {
            return JsonResult.errorMsg("购物车数据不支持");
        }

        List<CartBO> cartBOS = JsonUtils.jsonToList(shopCartCache, CartBO.class);

        if (CollectionUtils.isEmpty(cartBOS)) {
            return JsonResult.errorMsg("购物车数据不支持");
        }

        CreateOrderVO createOrderVO = orderService.createOrder(submitOrderBO, cartBOS);

        cartBOS.removeAll(createOrderVO.getNeedRemoveBOS());

        CookieUtils.setCookie(request, response, SHOP_CART_NAME, JsonUtils.objectToJson(createOrderVO));

        JsonResult result = sendRequest2PayCenter(createOrderVO.getMerchantOrdersVO());

        if (Objects.nonNull(result) && result.getStatus() != 200) {
            return JsonResult.errorMsg("支付中心创建订单失败");
        }

        return JsonResult.ok(createOrderVO.getOrderId());
    }

    @ApiOperation(value = "微信支付结果回调", notes = "微信支付结果回调", httpMethod = "POST")
    @PostMapping("notifyMerchantOrderPaid")
    public Integer wxPayCallback(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "轮询支付结果", notes = "轮询支付结果", httpMethod = "POST")
    @PostMapping("getPaidOrderInfo")
    public JsonResult getPaidOrderInfo(@RequestParam String orderId) {
        return JsonResult.ok(orderService.queryOrderStatus(orderId));
    }

    private JsonResult sendRequest2PayCenter(MerchantOrdersVO merchantOrdersVO) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");

        HttpEntity<MerchantOrdersVO> httpEntity = new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JsonResult> jsonResponse = restTemplate.postForEntity(PAY_CENTER_URL, httpEntity, JsonResult.class);
        return jsonResponse.getBody();
    }
}
