package com.tristeza.mapper;

import com.tristeza.item.pojo.Items;
import com.tristeza.item.vo.CartVO;
import com.tristeza.my.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsMapper extends MyMapper<Items> {
    List<CartVO> queryItemsBySpecIds(@Param("specIds") List<String> specIds);

    Integer decreaseItemSpecStock(String specId, Integer pendingCounts);
}