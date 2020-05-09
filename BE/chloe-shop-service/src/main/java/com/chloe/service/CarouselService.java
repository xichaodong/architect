package com.chloe.service;

import com.chloe.model.pojo.Carousel;

import java.util.List;

public interface CarouselService {
    /**
     * @param isShow 是否显示
     * @return {@link Carousel}
     */

    List<Carousel> queryAll(Integer isShow);
}
