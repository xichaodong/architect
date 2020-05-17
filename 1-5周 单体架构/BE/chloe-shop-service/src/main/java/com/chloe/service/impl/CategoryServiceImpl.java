package com.chloe.service.impl;

import com.chloe.common.enums.CategoryLevelEnum;
import com.chloe.mapper.CategoryMapper;
import com.chloe.model.pojo.Category;
import com.chloe.model.vo.SubCategoryVO;
import com.chloe.model.vo.SuggestItemVO;
import com.chloe.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Category> queryAllRootCategory() {
        Example categoryExample = new Example(Category.class);
        Example.Criteria criteria = categoryExample.createCriteria();

        criteria.andEqualTo("type", CategoryLevelEnum.ROOT.type);

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<SubCategoryVO> querySubCategoryByRootId(Integer rootId) {
        return categoryMapper.getSubCategoryList(rootId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<SuggestItemVO> querySuggestItemInfo(Integer rootCatId) {

        return categoryMapper.getSuggestItemInfo(rootCatId);
    }
}
