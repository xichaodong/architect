package com.tristeza.item.api;

import com.tristeza.item.model.pojo.Items;
import com.tristeza.item.model.pojo.ItemsImg;
import com.tristeza.item.model.pojo.ItemsParam;
import com.tristeza.item.model.pojo.ItemsSpec;
import com.tristeza.item.model.vo.CartVO;
import com.tristeza.item.model.vo.CommentCountsVO;
import com.tristeza.cloud.model.pojo.PagedGridResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("item-api")
@FeignClient("item-api")
public interface ItemApi {
    /**
     * 根据商品id查询商品详情
     *
     * @param itemId 商品id
     * @return {@link Items}
     */
    @GetMapping("item")
    Items queryItemById(@RequestParam("itemId") String itemId);

    @GetMapping("itemSpec")
    ItemsSpec queryItemSpecBySpecId(@RequestParam("specId") String specId);

    @GetMapping("primaryImg")
    String queryItemMainImgByItemId(@RequestParam("itemId") String itemId);

    @PostMapping("decreaseStock")
    void decreaseItemSpecStock(@RequestParam("specId") String specId, @RequestParam("buyCounts") Integer buyCounts);
}
