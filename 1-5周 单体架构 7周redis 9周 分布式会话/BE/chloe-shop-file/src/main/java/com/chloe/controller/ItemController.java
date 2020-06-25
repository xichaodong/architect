package com.chloe.controller;

import com.chloe.common.utils.JsonResult;
import com.chloe.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("items")
public class ItemController {
    public static final int SEARCH_PAGE_SIZE = 20;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE = 1;

    @Resource
    private FileService searchService;

    @GetMapping("/es/search")
    public JsonResult search(String keywords, String sort, Integer page, Integer pageSize) {
        if (Objects.isNull(page)) {
            page = DEFAULT_PAGE;
        }

        if (Objects.isNull(pageSize)) {
            pageSize = SEARCH_PAGE_SIZE;
        }

        page--;

        return JsonResult.ok(searchService.searchItems(keywords, sort, page, pageSize));
    }
}
