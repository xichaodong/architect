package com.chloe.service;

import com.chloe.model.pojo.Category;
import com.chloe.model.vo.SubCategoryVO;
import com.chloe.model.vo.SuggestItemVO;

import java.util.List;

public interface CategoryService {
    List<Category> queryAllRootCategory();

    List<SubCategoryVO> querySubCategoryByRootId(Integer rootId);

    List<SuggestItemVO> querySuggestItemInfo(Integer rootCatId);
}
