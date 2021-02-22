package com.tristeza.item.mapper;

import com.tristeza.item.model.bo.CenterCommentBO;
import com.tristeza.item.model.pojo.ItemsComments;
import com.tristeza.item.model.vo.CenterCommentVO;
import com.tristeza.item.model.vo.ItemCommentVO;
import com.tristeza.cloud.common.my.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemsCommentsMapper extends MyMapper<ItemsComments> {
    List<ItemCommentVO> queryItemComments(String itemId, Integer commentLevel);

    void saveComment(String userId, @Param("comments") List<CenterCommentBO> comments);

    List<CenterCommentVO> queryUserComments(String userId);
}