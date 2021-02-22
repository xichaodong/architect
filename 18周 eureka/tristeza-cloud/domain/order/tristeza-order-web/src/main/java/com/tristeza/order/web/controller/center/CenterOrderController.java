package com.tristeza.order.web.controller.center;

import com.tristeza.cloud.model.pojo.JsonResult;
import com.tristeza.order.service.center.CenterOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@Api(value = "用户中心订单相关操作", tags = {"用户中心订单相关接口"})
@RestController
@RequestMapping("myorders")
public class CenterOrderController {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Resource
    private CenterOrderService centerOrderService;

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("query")
    public JsonResult queryUserOrder(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "页码", required = false, example = "1")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页的数量", required = false, example = "10")
            @RequestParam Integer pageSize) {

        if (Objects.isNull(page)) {
            page = DEFAULT_PAGE;
        }

        if (Objects.isNull(pageSize)) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return JsonResult.ok(centerOrderService.queryCenterUserOrder(userId, orderStatus, page, pageSize));
    }

    @ApiOperation(value = "商家发货", notes = "商家发货", httpMethod = "GET")
    @GetMapping("deliver")
    public JsonResult deliver(@RequestParam String orderId) {
        if (Objects.isNull(orderId)) {
            return JsonResult.errorMsg("订单id不能为空");
        }
        if (!centerOrderService.updateDeliverOrderStatus(orderId)) {
            return JsonResult.errorMsg("商家发货失败");
        }

        return JsonResult.ok();
    }

    @ApiOperation(value = "用户确认收货", notes = "用户确认收货", httpMethod = "POST")
    @PostMapping("confirmReceive")
    public JsonResult confirmReceive(@RequestParam String orderId, @RequestParam String userId) {
        if (Objects.isNull(orderId)) {
            return JsonResult.errorMsg("订单id不能为空");
        }
        if (Objects.isNull(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }
        if (!checkUserOrder(userId, orderId)) {
            return JsonResult.errorMsg("订单不存在");
        }

        if (!centerOrderService.updateConfirmDeliverOrderStatus(orderId)) {
            return JsonResult.errorMsg("确认收货失败");
        }

        return JsonResult.ok();
    }

    @ApiOperation(value = "用户删除订单", notes = "用户删除订单", httpMethod = "POST")
    @PostMapping("delete")
    public JsonResult delete(@RequestParam String orderId, @RequestParam String userId) {
        if (Objects.isNull(orderId)) {
            return JsonResult.errorMsg("订单id不能为空");
        }
        if (Objects.isNull(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        if (!centerOrderService.deleteOrder(userId, orderId)) {
            return JsonResult.errorMsg("删除订单失败");
        }

        return JsonResult.ok();
    }

    @ApiOperation(value = "获取订单状态数概况", notes = "获取订单状态数概况", httpMethod = "GET")
    @PostMapping("statusCounts")
    public JsonResult orderStatusCount(@RequestParam String userId) {
        if (Objects.isNull(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        return JsonResult.ok(centerOrderService.getOrderStatusCounts(userId));
    }

    @ApiOperation(value = "获取订单轨迹", notes = "获取订单轨迹", httpMethod = "POST")
    @PostMapping("trend")
    public JsonResult orderTrend(@ApiParam(name = "userId", value = "用户id", required = true)
                                 @RequestParam String userId,
                                 @ApiParam(name = "page", value = "页码", required = false, example = "1")
                                 @RequestParam Integer page,
                                 @ApiParam(name = "pageSize", value = "每页的数量", required = false, example = "10")
                                 @RequestParam Integer pageSize) {
        if (Objects.isNull(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        return JsonResult.ok(centerOrderService.getUserOrderTrend(userId, page, pageSize));
    }

    /**
     * 订单校验，看是否是恶意修改
     *
     * @param userId  用户id
     * @param orderId 订单id
     * @return 订单是否存在
     */
    private boolean checkUserOrder(String userId, String orderId) {
        return Objects.nonNull(centerOrderService.queryUserOrder(userId, orderId));
    }
}
