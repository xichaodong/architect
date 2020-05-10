package com.chloe.controller;

import com.chloe.common.utils.JsonResult;
import com.chloe.common.utils.PagedGridResult;
import com.chloe.model.vo.ItemDetailVO;
import com.chloe.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "商品", tags = {"商品详情的相关接口"})
@RestController
@RequestMapping("items")
public class ItemController {
    @Resource
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("info/{itemId}")
    public JsonResult itemDetail(
            @ApiParam(name = "itemId", value = "商品id", required = true, example = "cake-001")
            @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg("商品id不能为空");
        }

        ItemDetailVO itemDetailVO = new ItemDetailVO();
        itemDetailVO.setItem(itemService.queryItemById(itemId));
        itemDetailVO.setItemImgList(itemService.queryItemImgById(itemId));
        itemDetailVO.setItemSpecList(itemService.queryItemSpecById(itemId));
        itemDetailVO.setItemParams(itemService.queryItemParam(itemId));

        return JsonResult.ok(itemDetailVO);
    }

    @ApiOperation(value = "查询商品评价数量", notes = "查询商品评价数量", httpMethod = "GET")
    @GetMapping("commentLevel")
    public JsonResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true, example = "cake-001")
            @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg("商品id不能为空");
        }

        return JsonResult.ok(itemService.queryCommentCounts(itemId));
    }

    @ApiOperation(value = "查询商品评价", notes = "查询商品评价", httpMethod = "GET")
    @GetMapping("comments")
    public PagedGridResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true, example = "cake-1001")
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价级别", required = true, example = "1")
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "页码", required = true, example = "1")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页的数量", required = true, example = "10")
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg("商品id不能为空");
        }

        return JsonResult.ok(itemService.queryCommentCounts(itemId));
    }
}
