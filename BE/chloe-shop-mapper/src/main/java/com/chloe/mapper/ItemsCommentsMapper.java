package com.chloe.mapper;

import com.chloe.model.vo.center.CenterCommentVO;
import com.chloe.model.bo.center.CenterCommentBO;
import com.chloe.model.vo.ItemCommentVO;
import my.mapper.MyMapper;
import com.chloe.model.pojo.ItemsComments;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsCommentsMapper extends MyMapper<ItemsComments> {
    List<ItemCommentVO> queryItemComments(String itemId, Integer commentLevel);

    void saveComment(String userId, @Param("comments") List<CenterCommentBO> comments);

    List<CenterCommentVO> queryUserComments(String userId);
}