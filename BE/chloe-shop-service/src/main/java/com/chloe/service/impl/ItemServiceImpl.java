package com.chloe.service.impl;

import com.chloe.mapper.ItemsImgMapper;
import com.chloe.mapper.ItemsMapper;
import com.chloe.mapper.ItemsParamMapper;
import com.chloe.mapper.ItemsSpecMapper;
import com.chloe.model.pojo.Items;
import com.chloe.model.pojo.ItemsImg;
import com.chloe.model.pojo.ItemsParam;
import com.chloe.model.pojo.ItemsSpec;
import com.chloe.model.vo.CommentCountsVO;
import com.chloe.service.ItemService;
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

        return null;
    }
}
