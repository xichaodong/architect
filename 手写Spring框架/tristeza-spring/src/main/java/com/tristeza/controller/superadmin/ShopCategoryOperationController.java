package com.tristeza.controller.superadmin;

import com.tristeza.model.bo.ShopCategory;
import com.tristeza.model.dto.Result;
import com.tristeza.service.solo.ShopCategoryService;
import com.tristeza.springframework.core.annotation.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ShopCategoryOperationController {
    private ShopCategoryService shopCategoryService;

    public Result<Boolean> addShopCategory(HttpServletRequest req, HttpServletResponse resp) {
        //TODO:参数校验以及请求参数转化
        return shopCategoryService.addShopCategory(new ShopCategory());
    }

    public Result<Boolean> removeShopCategory(HttpServletRequest req, HttpServletResponse resp) {
        //TODO:参数校验以及请求参数转化
        return shopCategoryService.removeShopCategory(1);
    }

    public Result<Boolean> modifyShopCategory(HttpServletRequest req, HttpServletResponse resp) {
        //TODO:参数校验以及请求参数转化
        return shopCategoryService.modifyShopCategory(new ShopCategory());
    }

    public Result<ShopCategory> queryShopCategoryById(HttpServletRequest req, HttpServletResponse resp) {
        //TODO:参数校验以及请求参数转化
        return shopCategoryService.queryShopCategoryById(1);
    }

    public Result<List<ShopCategory>> queryShopCategory(HttpServletRequest req, HttpServletResponse resp) {
        //TODO:参数校验以及请求参数转化
        return shopCategoryService.queryShopCategory(null, 1, 100);
    }
}
