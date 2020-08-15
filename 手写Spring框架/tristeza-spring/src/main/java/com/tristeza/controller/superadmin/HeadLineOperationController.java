package com.tristeza.controller.superadmin;

import com.tristeza.model.bo.HeadLine;
import com.tristeza.model.dto.Result;
import com.tristeza.service.solo.HeadLineService;
import com.tristeza.springframework.core.annotation.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class HeadLineOperationController {
    private HeadLineService headLineService;


    public void removeHeadLine() {
        System.out.println("删除HeadLine");
    }

    public Result<Boolean> modifyHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        //TODO:参数校验以及请求参数转化
        return headLineService.modifyHeadLine(new HeadLine());
    }

    public Result<HeadLine> queryHeadLineById(HttpServletRequest req, HttpServletResponse resp) {
        //TODO:参数校验以及请求参数转化
        return headLineService.queryHeadLineById(1);
    }

    public Result<List<HeadLine>> queryHeadLine() {
        return headLineService.queryHeadLine(null, 1, 100);
    }
}
