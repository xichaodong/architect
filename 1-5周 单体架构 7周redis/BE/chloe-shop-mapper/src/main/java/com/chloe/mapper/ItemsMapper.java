package com.chloe.mapper;

import com.chloe.model.vo.CartVO;
import com.chloe.model.vo.SearchItemVO;
import my.mapper.MyMapper;
import com.chloe.model.pojo.Items;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsMapper extends MyMapper<Items> {
    List<SearchItemVO> searchItems(String keywords, String sort);

    List<SearchItemVO> searchItemsByThirdCatId(Integer categoryId, String sort);

    List<CartVO> queryItemsBySpecIds(@Param("specIds") List<String> specIds);

    Integer decreaseItemSpecStock(String specId, Integer pendingCounts);
}