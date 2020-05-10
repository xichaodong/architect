package com.chloe.mapper;

import com.chloe.model.vo.ItemCommentVO;
import my.mapper.MyMapper;
import com.chloe.model.pojo.ItemsComments;

import java.util.List;

public interface ItemsCommentsMapper extends MyMapper<ItemsComments> {
    List<ItemCommentVO> queryItemComments(String itemId, String commentLevel);
}