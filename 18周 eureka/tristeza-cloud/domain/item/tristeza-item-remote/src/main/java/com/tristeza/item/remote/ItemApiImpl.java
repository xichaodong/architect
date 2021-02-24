package com.tristeza.item.remote;

import com.tristeza.item.api.ItemApi;
import com.tristeza.item.model.pojo.Items;
import com.tristeza.item.model.pojo.ItemsSpec;
import com.tristeza.service.ItemService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2021/2/24 11:49 上午
 */
@RestController
public class ItemApiImpl implements ItemApi {
    @Resource
    private ItemService itemService;

    @Override
    public Items queryItemById(String itemId) {
        return itemService.queryItemById(itemId);
    }

    @Override
    public ItemsSpec queryItemSpecBySpecId(String specId) {
        return itemService.queryItemSpecBySpecId(specId);
    }

    @Override
    public String queryItemMainImgByItemId(String itemId) {
        return itemService.queryItemMainImgByItemId(itemId);
    }

    @Override
    public void decreaseItemSpecStock(String specId, Integer buyCounts) {
        itemService.decreaseItemSpecStock(specId, buyCounts);
    }
}
