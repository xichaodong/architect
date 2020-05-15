package com.chloe.controller.center;

import com.chloe.common.enums.BooleanEnum;
import com.chloe.common.utils.JsonResult;
import com.chloe.model.bo.center.CenterCommentBO;
import com.chloe.model.pojo.Orders;
import com.chloe.service.center.CenterCommentService;
import com.chloe.service.center.CenterOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Api(value = "用户中心评论操作", tags = {"用户中心评论相关接口"})
@RestController
@RequestMapping("mycomments")
public class CenterCommentController {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE = 1;

    @Resource
    private CenterCommentService centerCommentService;
    @Resource
    private CenterOrderService centerOrderService;

    @ApiOperation(value = "查询待评价订单", notes = "查询待评价订单", httpMethod = "POST")
    @PostMapping("pending")
    public JsonResult pending(@RequestParam String orderId, @RequestParam String userId) {
        JsonResult result = checkUserOrder(userId, orderId);

        if (result.getStatus() != HttpStatus.OK.value()) {
            return result;
        }

        Orders orders = (Orders) result.getData();

        if (BooleanEnum.TRUE.type.equals(orders.getIsComment())) {
            return JsonResult.errorMsg("该笔订单已评价");
        }

        return JsonResult.ok(centerCommentService.queryPendingComments(orderId));
    }

    @ApiOperation(value = "保存用户评价", notes = "保存用户评价", httpMethod = "POST")
    @PostMapping("saveList")
    public JsonResult save(@RequestParam String orderId, @RequestParam String userId,
                           @RequestBody List<CenterCommentBO> commentList) {
        checkUserOrder(userId, orderId);

        centerCommentService.saveComments(userId, orderId, commentList);

        return JsonResult.ok();
    }

    @ApiOperation(value = "查询用户评价", notes = "查询用户评价", httpMethod = "POST")
    @PostMapping("query")
    public JsonResult query(@ApiParam(name = "userId", value = "用户id", required = true)
                            @RequestParam String userId,
                            @ApiParam(name = "page", value = "页码", required = false, example = "1")
                            @RequestParam Integer page,
                            @ApiParam(name = "pageSize", value = "每页的数量", required = false, example = "10")
                            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        if (Objects.isNull(page)) {
            page = DEFAULT_PAGE;
        }

        if (Objects.isNull(pageSize)) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return JsonResult.ok(centerCommentService.queryUserComment(userId, page, pageSize));
    }


    /**
     * 订单校验，看是否是恶意修改
     *
     * @param userId  用户id
     * @param orderId 订单id
     * @return 订单是否存在
     */
    private JsonResult checkUserOrder(String userId, String orderId) {
        if (Objects.isNull(orderId)) {
            return JsonResult.errorMsg("订单id不能为空");
        }
        if (Objects.isNull(userId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        Orders orders = centerOrderService.queryUserOrder(userId, orderId);

        if (Objects.isNull(orders)) {
            return JsonResult.errorMsg("对应订单不存在");
        }

        return JsonResult.ok(orders);
    }
}
