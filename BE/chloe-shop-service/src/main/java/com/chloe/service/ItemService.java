package com.chloe.service;

import com.chloe.model.pojo.Items;
import com.chloe.model.pojo.ItemsImg;
import com.chloe.model.pojo.ItemsParam;
import com.chloe.model.pojo.ItemsSpec;
import com.chloe.model.vo.CommentCountsVO;

import java.util.List;

public interface ItemService {
    /**
     * 根据商品id查询商品详情
     *
     * @param itemId 商品id
     * @return {@link Items}
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品规格
     *
     * @param itemId 商品id
     * @return {@link Items}
     */
    List<ItemsSpec> queryItemSpecById(String itemId);

    /**
     * 根据商品id查询商品图片
     *
     * @param itemId 商品id
     * @return {@link Items}
     */
    List<ItemsImg> queryItemImgById(String itemId);

    /**
     * 根据商品id查询商品参数
     *
     * @param itemId 商品id
     * @return {@link Items}
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询商品评价数
     *
     * @param itemId 商品id
     * @return {@link CommentCountsVO}
     */
    CommentCountsVO queryCommentCounts(String itemId);
}
