package com.tristeza.item.model.vo;

import com.tristeza.item.model.pojo.Items;
import com.tristeza.item.model.pojo.ItemsImg;
import com.tristeza.item.model.pojo.ItemsParam;
import com.tristeza.item.model.pojo.ItemsSpec;

import java.util.List;

/**
 * 商品详细信息VO
 */
public class ItemDetailVO {
    private Items item;

    private ItemsParam itemParams;

    private List<ItemsImg> itemImgList;

    private List<ItemsSpec> itemSpecList;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }

    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }
}
