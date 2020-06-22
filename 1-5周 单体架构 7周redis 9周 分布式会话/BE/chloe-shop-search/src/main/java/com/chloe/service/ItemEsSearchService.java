package com.chloe.service;

import com.chloe.common.utils.PagedGridResult;

public interface ItemEsSearchService {
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);
}
