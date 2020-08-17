package com.tristeza.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tristeza.api.ItemApi;
import com.tristeza.enums.BooleanEnum;
import com.tristeza.enums.CommentLevelEnum;
import com.tristeza.item.pojo.*;
import com.tristeza.item.vo.CartVO;
import com.tristeza.item.vo.CommentCountsVO;
import com.tristeza.item.vo.ItemCommentVO;
import com.tristeza.mapper.*;
import com.tristeza.pojo.PagedGridResult;
import com.tristeza.utils.DesensitizationUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
public class ItemServiceImpl implements ItemApi {
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
    @Resource
    private RedissonClient redissonClient;

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

        return buildPagedResult(itemComments, page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CartVO> queryItemBySpecIds(String spedIdStr) {
        List<String> specIds = Arrays.asList(spedIdStr.split(","));

        return itemsMapper.queryItemsBySpecIds(specIds);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsSpec queryItemSpecBySpecId(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String queryItemMainImgByItemId(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setIsMain(BooleanEnum.TRUE.type);
        itemsImg.setItemId(itemId);

        ItemsImg mainImg = itemsImgMapper.selectOne(itemsImg);

        return Objects.isNull(mainImg) ? "" : mainImg.getUrl();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseItemSpecStock(String specId, Integer buyCounts) {
        RLock lock = redissonClient.getLock(String.format("item_lock_%s", specId));
        lock.lock(5, TimeUnit.SECONDS);

        try {
            boolean isSuccess = itemsMapper.decreaseItemSpecStock(specId, buyCounts) > 0;
            if (!isSuccess) {
                throw new RuntimeException("订单创建失败,原因：库存不足!");
            }
        } finally {
            lock.unlock();
        }
    }

    private Integer getCommentCountsByLevel(String itemId, Integer level) {
        ItemsComments itemsComments = new ItemsComments();

        itemsComments.setCommentLevel(level);
        itemsComments.setItemId(itemId);

        return itemsCommentsMapper.selectCount(itemsComments);
    }

    private PagedGridResult buildPagedResult(List<?> records, Integer page) {
        PagedGridResult pagedGridResult = new PagedGridResult();
        PageInfo<?> pageInfo = new PageInfo<>(records);

        pagedGridResult.setPage(page);
        pagedGridResult.setRows(pageInfo.getList());
        pagedGridResult.setTotal(pageInfo.getPages());
        pagedGridResult.setRecords(pageInfo.getTotal());

        return pagedGridResult;
    }
}
