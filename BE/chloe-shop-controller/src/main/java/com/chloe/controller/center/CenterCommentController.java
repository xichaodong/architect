package com.chloe.controller.center;

import com.chloe.common.enums.BooleanEnum;
import com.chloe.common.utils.JsonResult;
import com.chloe.model.pojo.Orders;
import com.chloe.service.center.CenterCommentService;
import com.chloe.service.center.CenterOrderService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@Api(value = "用户中心评论操作", tags = {"用户中心评论相关接口"})
@RestController
@RequestMapping("mycomments")
public class CenterCommentController {
    @Resource
    private CenterCommentService centerCommentService;
    @Resource
    private CenterOrderService centerOrderService;

    @PostMapping("pending")
    public JsonResult pending(@RequestParam String orderId, @RequestParam String userId) {
        if (Objects.isNull(orderId)) {
            return JsonResult.errorMsg("订单id不能为空");
        }
        if (Objects.isNull(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }
        Orders orders = checkUserOrder(userId, orderId);

        if (Objects.isNull(orders)) {
            return JsonResult.errorMsg("对应订单不存在");
        }
        if (BooleanEnum.TRUE.type.equals(orders.getIsComment())) {
            return JsonResult.errorMsg("该笔订单已评价");
        }

        return JsonResult.ok(centerCommentService.queryPendingComments(orderId));
    }


    /**
     * 订单校验，看是否是恶意修改
     *
     * @param userId  用户id
     * @param orderId 订单id
     * @return 订单是否存在
     */
    private Orders checkUserOrder(String userId, String orderId) {
        return centerOrderService.queryUserOrder(userId, orderId);
    }
}
