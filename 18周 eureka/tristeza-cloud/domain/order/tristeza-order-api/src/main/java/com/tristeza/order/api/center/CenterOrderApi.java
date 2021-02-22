package com.tristeza.order.api.center;

import com.tristeza.cloud.model.pojo.JsonResult;
import com.tristeza.cloud.model.pojo.PagedGridResult;
import com.tristeza.order.model.pojo.Orders;
import com.tristeza.order.model.vo.center.CenterOrderStatusCountsVO;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("center-order-api")
public interface CenterOrderApi {
    @GetMapping("center/user/order")
    PagedGridResult queryCenterUserOrder(@RequestParam("userId") String userId,
                                         @RequestParam("orderStatus") Integer orderStatus,
                                         @RequestParam(value = "page", required = false) Integer page,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @PutMapping("deliver/order/status")
    boolean updateDeliverOrderStatus(@RequestParam("orderId") String orderId);

    @PutMapping("confirm/deliver/order/status")
    boolean updateConfirmDeliverOrderStatus(@RequestParam("orderId") String orderId);

    @DeleteMapping("order")
    boolean deleteOrder(@RequestParam("userId") String userId, @RequestParam("orderId") String orderId);

    @GetMapping("user/order")
    Orders queryUserOrder(@RequestParam("userId") String userId, @RequestParam("orderId") String orderId);

    @GetMapping("order/status/count")
    CenterOrderStatusCountsVO getOrderStatusCounts(@RequestParam("userId") String userId);

    @GetMapping("user/order/trend")
    PagedGridResult getUserOrderTrend(@RequestParam("userId") String userId,
                                      @RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @GetMapping("check/user/order")
    JsonResult checkUserOrder(String userId, String orderId);
}
