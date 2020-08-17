package com.tristeza.api;

import com.tristeza.item.bo.CenterCommentBO;
import com.tristeza.pojo.PagedGridResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author chaodong.xi
 * @date 2020/8/16 6:03 下午
 */
@RequestMapping("item-comments-api")
public interface ItemCommentsApi {
    @GetMapping("myComments")
    PagedGridResult queryUserComment(@RequestParam("userId") String userId,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @PostMapping("saveComments")
    void saveComments(String userId, String orderId, List<CenterCommentBO> comments);
}
