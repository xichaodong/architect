package com.chloe.model.vo;

import java.util.List;

/**
 * 二级分类VO
 */
public class SubCategoryVO {
    private Integer id;

    private String name;

    private String type;

    private Integer fatherId;

    private List<ThirdLevelCategoryVO> thirdLevelCats;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public List<ThirdLevelCategoryVO> getThirdLevelCats() {
        return thirdLevelCats;
    }

    public void setThirdLevelCats(List<ThirdLevelCategoryVO> thirdLevelCats) {
        this.thirdLevelCats = thirdLevelCats;
    }
}
