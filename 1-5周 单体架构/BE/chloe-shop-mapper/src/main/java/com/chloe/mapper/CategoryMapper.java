package com.chloe.mapper;

import com.chloe.model.vo.SubCategoryVO;
import com.chloe.model.vo.SuggestItemVO;
import my.mapper.MyMapper;
import com.chloe.model.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapper extends MyMapper<Category> {
    List<SubCategoryVO> getSubCategoryList(Integer rootCatId);

    List<SuggestItemVO> getSuggestItemInfo(Integer rootCatId);
}