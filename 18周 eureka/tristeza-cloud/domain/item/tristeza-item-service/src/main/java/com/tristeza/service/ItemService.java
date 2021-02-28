package com.tristeza.service;

import com.tristeza.cloud.model.pojo.PagedGridResult;
import com.tristeza.item.model.pojo.Items;
import com.tristeza.item.model.pojo.ItemsImg;
import com.tristeza.item.model.pojo.ItemsParam;
import com.tristeza.item.model.pojo.ItemsSpec;
import com.tristeza.item.model.vo.CartVO;
import com.tristeza.item.model.vo.CommentCountsVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ItemService {
    /**
     * 根据商品id查询商品详情
     *
     * @param itemId 商品id
     * @return {@link Items}
     */
    Items queryItemById(@RequestParam("itemId") String itemId);

    /**
     * 根据商品id查询商品规格
     *
     * @param itemId 商品id
     * @return {@link Items}
     */
    List<ItemsSpec> queryItemSpecById(@RequestParam("itemId") String itemId);

    /**
     * 根据商品id查询商品图片
     *
     * @param itemId 商品id
     * @return {@link Items}
     */
    List<ItemsImg> queryItemImgById(@RequestParam("itemId") String itemId);

    /**
     * 根据商品id查询商品参数
     *
     * @param itemId 商品id
     * @return {@link Items}
     */
    ItemsParam queryItemParam(@RequestParam("itemId") String itemId);

    /**
     * 根据商品id查询商品评价数
     *
     * @param itemId 商品id
     * @return {@link CommentCountsVO}
     */
    CommentCountsVO queryCommentCounts(@RequestParam("itemId") String itemId);

    /**
     * 根据评价等级分页查询商品评价
     *
     * @param itemId       商品id
     * @param commentLevel 评价级别
     * @return {@link PagedGridResult}
     */
    PagedGridResult queryPagedItemComment(@RequestParam("itemId") String itemId,
                                          @RequestParam(value = "commentLevel", required = false) Integer commentLevel,
                                          @RequestParam(value = "page", required = false) Integer page,
                                          @RequestParam(value = "pageSize", required = false) Integer pageSize);

    List<CartVO> queryItemBySpecIds(@RequestParam("spedIdStr") String spedIdStr);

    ItemsSpec queryItemSpecBySpecId(@RequestParam("specId") String specId);

    String queryItemMainImgByItemId(@RequestParam("itemId") String itemId);

    void decreaseItemSpecStock(@RequestParam("specId") String specId, @RequestParam("buyCounts") Integer buyCounts);
}
