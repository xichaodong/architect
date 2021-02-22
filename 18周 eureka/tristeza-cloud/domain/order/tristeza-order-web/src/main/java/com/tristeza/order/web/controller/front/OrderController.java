package com.tristeza.order.web.controller.front;

import com.tristeza.cart.model.bo.CartBO;
import com.tristeza.cloud.common.utils.CookieUtils;
import com.tristeza.cloud.common.utils.JsonUtils;
import com.tristeza.cloud.common.utils.RedisOperator;
import com.tristeza.cloud.model.enums.OrderStatusEnum;
import com.tristeza.cloud.model.enums.PayMethodEnum;
import com.tristeza.cloud.model.pojo.JsonResult;
import com.tristeza.order.model.bo.front.PlaceOrderBO;
import com.tristeza.order.model.bo.front.SubmitOrderBO;
import com.tristeza.order.model.vo.front.CreateOrderVO;
import com.tristeza.order.model.vo.front.MerchantOrdersVO;
import com.tristeza.order.service.front.OrderService;
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

        CreateOrderVO createOrderVO = orderService.createOrder(new PlaceOrderBO(submitOrderBO, cartBOS));

        cartBOS.removeAll(createOrderVO.getNeedRemoveBOS());

        if (CollectionUtils.isEmpty(cartBOS)) {
            redisOperator.del(shopCacheKey);
        } else {
            redisOperator.set(shopCacheKey, JsonUtils.objectToJson(cartBOS));
        }

        CookieUtils.setCookie(request, response, SHOP_CART_NAME, JsonUtils.objectToJson(cartBOS), true);

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
