package com.tristeza.item.mapper;

import com.tristeza.item.model.pojo.Items;
import com.tristeza.item.model.vo.CartVO;
import com.tristeza.cloud.common.my.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsMapper extends MyMapper<Items> {
    List<CartVO> queryItemsBySpecIds(@Param("specIds") List<String> specIds);

    Integer decreaseItemSpecStock(String specId, Integer pendingCounts);
}