package com.tristeza.mapper;

import com.tristeza.item.bo.CenterCommentBO;
import com.tristeza.item.pojo.ItemsComments;
import com.tristeza.item.vo.CenterCommentVO;
import com.tristeza.item.vo.ItemCommentVO;
import com.tristeza.my.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsCommentsMapper extends MyMapper<ItemsComments> {
    List<ItemCommentVO> queryItemComments(String itemId, Integer commentLevel);

    void saveComment(String userId, @Param("comments") List<CenterCommentBO> comments);

    List<CenterCommentVO> queryUserComments(String userId);
}