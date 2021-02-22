package com.tristeza.item.web.controller;

import com.tristeza.cloud.model.pojo.JsonResult;
import com.tristeza.item.model.bo.CenterCommentBO;
import com.tristeza.service.ItemCommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Api(value = "用户中心评论操作", tags = {"用户中心评论相关接口"})
@RestController
@RequestMapping("mycomments")
public class ItemsCommentsController {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE = 1;

    @Resource
    private ItemCommentsService itemCommentsService;

    @ApiOperation(value = "保存用户评价", notes = "保存用户评价", httpMethod = "POST")
    @PostMapping("saveList")
    public JsonResult save(@RequestParam String orderId, @RequestParam String userId,
                           @RequestBody List<CenterCommentBO> commentList) {
        //TODO
//        checkUserOrder(userId, orderId);

        itemCommentsService.saveComments(userId, orderId, commentList);

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

        return JsonResult.ok(itemCommentsService.queryUserComment(userId, page, pageSize));
    }
}
