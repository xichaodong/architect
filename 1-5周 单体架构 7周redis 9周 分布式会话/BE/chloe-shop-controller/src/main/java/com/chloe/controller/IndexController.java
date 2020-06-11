package com.chloe.controller;

import com.chloe.common.enums.BooleanEnum;
import com.chloe.common.utils.JsonResult;
import com.chloe.common.utils.JsonUtils;
import com.chloe.common.utils.RedisOperator;
import com.chloe.model.pojo.Carousel;
import com.chloe.model.pojo.Category;
import com.chloe.model.vo.SubCategoryVO;
import com.chloe.model.vo.SuggestItemVO;
import com.chloe.service.CarouselService;
import com.chloe.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {
    private static final String CAROUSEL_CACHE = "CAROUSEL";
    private static final String SUB_CATEGORY_CACHE_KEY = "SUB_CATEGORY";
    private static final String TOP_CATEGORY_CACHE_KEY = "TOP_CATEGORY";

    @Resource
    private CarouselService carouselService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private RedisOperator redisOperator;

    @GetMapping("session")
    public String setSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new User");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");

        return "ok";
    }

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("carousel")
    public JsonResult carousel() {
        String carouselCache = redisOperator.get(CAROUSEL_CACHE);

        if (StringUtils.isBlank(carouselCache)) {
            List<Carousel> carousels = carouselService.queryAll(BooleanEnum.TRUE.type);
            redisOperator.set(CAROUSEL_CACHE, JsonUtils.objectToJson(carousels));

            return JsonResult.ok(carousels);
        }

        return JsonResult.ok(JsonUtils.jsonToList(carouselCache, Carousel.class));
    }

    @ApiOperation(value = "获取首页大分类", notes = "获取首页大分类", httpMethod = "GET")
    @GetMapping("cats")
    public JsonResult rootCats() {
        String topCategoryCache = redisOperator.get(TOP_CATEGORY_CACHE_KEY);

        if (StringUtils.isBlank(topCategoryCache)) {
            List<Category> categories = categoryService.queryAllRootCategory();

            redisOperator.set(TOP_CATEGORY_CACHE_KEY, JsonUtils.objectToJson(categories));

            return JsonResult.ok(categories);
        }

        return JsonResult.ok(JsonUtils.jsonToList(topCategoryCache, Category.class));
    }

    @ApiOperation(value = "获取大分类的子分类", notes = "获取大分类的子分类", httpMethod = "GET")
    @GetMapping("subCat/{rootCatId}")
    public JsonResult rootCats(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true, example = "1")
            @PathVariable Integer rootCatId) {

        if (Objects.isNull(rootCatId)) {
            return JsonResult.errorMsg("分类id不能为空");
        }

        String cacheKey = String.format("%s:%d", SUB_CATEGORY_CACHE_KEY, rootCatId);

        String subCategoryCache = redisOperator.get(cacheKey);

        if (StringUtils.isBlank(subCategoryCache)) {
            List<SubCategoryVO> subCategories = categoryService.querySubCategoryByRootId(rootCatId);
            redisOperator.set(cacheKey, JsonUtils.objectToJson(subCategories));

            return JsonResult.ok(subCategories);
        }

        return JsonResult.ok(JsonUtils.jsonToList(subCategoryCache, SubCategoryVO.class));
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
