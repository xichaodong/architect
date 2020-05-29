package com.chloe.service.impl;

import com.chloe.mapper.CarouselMapper;
import com.chloe.model.pojo.Carousel;
import com.chloe.service.CarouselService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {
    @Resource
    private CarouselMapper carouselMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Carousel> queryAll(Integer isShow) {
        Example carouselExample = new Example(Carousel.class);
        carouselExample.orderBy("sort").desc();

        Example.Criteria criteria = carouselExample.createCriteria();
        criteria.andEqualTo("isShow", isShow);

        return carouselMapper.selectByExample(carouselExample);
    }
}
