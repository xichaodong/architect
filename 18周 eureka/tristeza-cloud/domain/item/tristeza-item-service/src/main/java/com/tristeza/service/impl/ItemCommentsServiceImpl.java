package com.tristeza.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tristeza.cloud.common.idworker.Sid;
import com.tristeza.cloud.model.pojo.PagedGridResult;
import com.tristeza.item.mapper.ItemsCommentsMapper;
import com.tristeza.item.model.bo.CenterCommentBO;
import com.tristeza.item.model.vo.CenterCommentVO;
import com.tristeza.service.ItemCommentsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemCommentsServiceImpl implements ItemCommentsService {
    @Resource
    private Sid sid;
    @Resource
    private ItemsCommentsMapper itemsCommentsMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String userId, String orderId, List<CenterCommentBO> comments) {
        comments.forEach(comment -> comment.setCommentId(sid.nextShort()));
        itemsCommentsMapper.saveComment(userId, comments);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryUserComment(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<CenterCommentVO> comments = itemsCommentsMapper.queryUserComments(userId);
        PageInfo<CenterCommentVO> pageInfo = new PageInfo<>(comments);

        PagedGridResult result = new PagedGridResult();
        result.setRows(pageInfo.getList());
        result.setTotal(pageInfo.getPages());
        result.setPage(page);
        result.setRecords(pageInfo.getTotal());

        return result;
    }
}
