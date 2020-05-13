package com.chloe.controller;

import com.chloe.common.enums.OrderStatusEnum;
import com.chloe.common.enums.PayMethodEnum;
import com.chloe.common.utils.CookieUtils;
import com.chloe.common.utils.JsonResult;
import com.chloe.model.bo.SubmitOrderBO;
import com.chloe.model.pojo.OrderStatus;
import com.chloe.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "订单相关操作", tags = {"订单相关接口"})
@RestController
@RequestMapping("orders")
public class OrderController {
    private static final String SHOP_CART_NAME = "shopcart";
    @Resource
    private OrderService orderService;

    @ApiOperation(value = "用户提交订单", notes = "用户提交订单", httpMethod = "POST")
    @PostMapping("create")
    public JsonResult createOrder(@RequestBody SubmitOrderBO submitOrderBO, HttpServletRequest request, HttpServletResponse response) {
        if (!PayMethodEnum.WX.type.equals(submitOrderBO.getPayMethod()) &&
                !PayMethodEnum.ALI_PAY.type.equals(submitOrderBO.getPayMethod())) {
            return JsonResult.errorMsg("支付方式不支持");
        }

        String orderId = orderService.createOrder(submitOrderBO);

        CookieUtils.setCookie(request, response, SHOP_CART_NAME, StringUtils.EMPTY);

        return JsonResult.ok(orderId);
    }

    @ApiOperation(value = "微信支付结果回调", notes = "微信支付结果回调", httpMethod = "POST")
    @PostMapping("notifyMerchantOrderPaid")
    public Integer wxPayCallback(String orderId) {
        orderService.updateOrderStatus(orderId, OrderStatusEnum.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }
}
