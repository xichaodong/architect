package com.chloe.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    public static final Integer SEARCH_PAGE_SIZE = 20;

    public static final Integer DEFAULT_PAGE = 1;
}
