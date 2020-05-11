package com.chloe.service.impl;

import com.chloe.common.enums.CommentLevelEnum;
import com.chloe.common.utils.DesensitizationUtil;
import com.chloe.common.utils.PagedGridResult;
import com.chloe.mapper.*;
import com.chloe.model.pojo.*;
import com.chloe.model.vo.CommentCountsVO;
import com.chloe.model.vo.ItemCommentVO;
import com.chloe.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Resource
    private ItemsMapper itemsMapper;
    @Resource
    private ItemsImgMapper itemsImgMapper;
    @Resource
    private ItemsSpecMapper itemsSpecMapper;
    @Resource
    private ItemsParamMapper itemsParamMapper;
    @Resource
    private ItemsCommentsMapper itemsCommentsMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> queryItemSpecById(String itemId) {
        Example itemSpecExample = new Example(ItemsSpec.class);
        Example.Criteria criteria = itemSpecExample.createCriteria();

        criteria.andEqualTo("itemId", itemId);

        return itemsSpecMapper.selectByExample(itemSpecExample);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> queryItemImgById(String itemId) {
        Example itemImgExample = new Example(ItemsImg.class);
        Example.Criteria criteria = itemImgExample.createCriteria();

        criteria.andEqualTo("itemId", itemId);

        return itemsImgMapper.selectByExample(itemImgExample);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam queryItemParam(String itemId) {
        Example itemParamExample = new Example(ItemsParam.class);
        Example.Criteria criteria = itemParamExample.createCriteria();

        criteria.andEqualTo("itemId", itemId);

        return itemsParamMapper.selectOneByExample(itemParamExample);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CommentCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCountsByLevel(itemId, CommentLevelEnum.GOOD.type);
        Integer normalCounts = getCommentCountsByLevel(itemId, CommentLevelEnum.NORMAL.type);
        Integer badCounts = getCommentCountsByLevel(itemId, CommentLevelEnum.BAD.type);

        CommentCountsVO commentCountsVO = new CommentCountsVO();

        commentCountsVO.setGoodCounts(goodCounts);
        commentCountsVO.setNormalCounts(normalCounts);
        commentCountsVO.setBadCounts(badCounts);
        commentCountsVO.setTotalCounts(goodCounts + normalCounts + badCounts);

        return commentCountsVO;
    }

    @Override
    public PagedGridResult queryPagedItemComment(String itemId, Integer commentLevel, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<ItemCommentVO> itemComments = itemsCommentsMapper.queryItemComments(itemId, commentLevel);

        itemComments.forEach(comment -> comment.setNickname(DesensitizationUtil.commonDisplay(comment.getNickname())));

        PageInfo<ItemCommentVO> pageInfo = new PageInfo<>(itemComments);

        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(page);
        pagedGridResult.setRows(itemComments);
        pagedGridResult.setTotal(pageInfo.getPages());
        pagedGridResult.setRecords(pageInfo.getTotal());

        return pagedGridResult;
    }

    Integer getCommentCountsByLevel(String itemId, Integer level) {
        ItemsComments itemsComments = new ItemsComments();

        itemsComments.setCommentLevel(level);
        itemsComments.setItemId(itemId);

        return itemsCommentsMapper.selectCount(itemsComments);
    }
}
