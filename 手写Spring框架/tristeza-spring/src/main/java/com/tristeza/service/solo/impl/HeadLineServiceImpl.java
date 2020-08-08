package com.tristeza.service.solo.impl;

import com.tristeza.model.dto.Result;
import com.tristeza.model.bo.HeadLine;
import com.tristeza.service.solo.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HeadLineServiceImpl implements HeadLineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Override
    public Result<Boolean> addHeadLine(HeadLine headLine) {
        LOGGER.info("addHeadLine被执行啦, lineName[{}],lineLink[{}],lineImg[{}], priority[{}]",
                headLine.getLineName(), headLine.getLineLink(), headLine.getLineImg(), headLine.getPriority());
        Result<Boolean> result = new Result<Boolean>();
        result.setCode(200);
        result.setMsg("请求成功啦");
        result.setData(true);
        return result;
    }

    @Override
    public Result<Boolean> removeHeadLine(int headLineId) {
        return null;
    }

    @Override
    public Result<Boolean> modifyHeadLine(HeadLine headLine) {
        return null;
    }

    @Override
    public Result<HeadLine> queryHeadLineById(int headLineId) {
        return null;
    }

    @Override
    public Result<List<HeadLine>> queryHeadLine(HeadLine headLineCondition, int pageIndex, int pageSize) {
        List<HeadLine> headLineList = new ArrayList<>();
        HeadLine headLine1 = new HeadLine();
        headLine1.setLineId(1L);
        headLine1.setLineName("头条1");
        headLine1.setLineLink("www.baidu.com");
        headLine1.setLineImg("头条图片1地址");
        headLineList.add(headLine1);
        HeadLine headLine2 = new HeadLine();
        headLine2.setLineId(2L);
        headLine2.setLineName("头条2");
        headLine2.setLineLink("www.google.com");
        headLine2.setLineImg("头条图片2地址");
        headLineList.add(headLine2);
        Result<List<HeadLine>> result = new Result<>();
        result.setData(headLineList);
        result.setCode(200);
        return result;
    }
}
