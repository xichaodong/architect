package com.chloe.controller;

import com.chloe.common.enums.BooleanEnum;
import com.chloe.common.utils.JsonResult;
import com.chloe.model.pojo.Carousel;
import com.chloe.model.pojo.Category;
import com.chloe.model.vo.SubCategoryVO;
import com.chloe.model.vo.SuggestItemVO;
import com.chloe.service.CarouselService;
import com.chloe.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {
    @Resource
    private CarouselService carouselService;
    @Resource
    private CategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("carousel")
    public JsonResult carousel() {
        List<Carousel> carousels = carouselService.queryAll(BooleanEnum.TRUE.type);
        return JsonResult.ok(carousels);
    }

    @ApiOperation(value = "获取首页大分类", notes = "获取首页大分类", httpMethod = "GET")
    @GetMapping("cats")
    public JsonResult rootCats() {
        List<Category> categories = categoryService.queryAllRootCategory();
        return JsonResult.ok(categories);
    }

    @ApiOperation(value = "获取大分类的子分类", notes = "获取大分类的子分类", httpMethod = "GET")
    @GetMapping("subCat/{rootCatId}")
    public JsonResult rootCats(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true, example = "1")
            @PathVariable Integer rootCatId) {

        if (Objects.isNull(rootCatId)) {
            return JsonResult.errorMsg("分类id不能为空");
        }

        List<SubCategoryVO> subCategories = categoryService.querySubCategoryByRootId(rootCatId);

        return JsonResult.ok(subCategories);
    }

    @ApiOperation(value = "查询每个一级分类下最新的推荐商品", notes = "查询每个一级分类下最新的推荐商品", httpMethod = "GET")
    @GetMapping("sixNewItems/{rootCatId}")
    public JsonResult suggestItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true, example = "1")
            @PathVariable Integer rootCatId) {

        if (Objects.isNull(rootCatId)) {
            return JsonResult.errorMsg("分类id不能为空");
        }

        List<SuggestItemVO> suggestItemInfos = categoryService.querySuggestItemInfo(rootCatId);

        return JsonResult.ok(suggestItemInfos);
    }
}
