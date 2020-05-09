package com.chloe.controller;

import com.chloe.common.utils.JsonResult;
import com.chloe.model.vo.ItemDetailVO;
import com.chloe.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
