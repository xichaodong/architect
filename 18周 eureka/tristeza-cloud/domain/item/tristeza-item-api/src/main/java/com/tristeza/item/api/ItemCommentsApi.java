package com.tristeza.item.api;

import com.tristeza.cloud.model.pojo.PagedGridResult;
import com.tristeza.item.model.bo.CenterCommentBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chaodong.xi
 * @date 2020/8/16 6:03 下午
 */
@RequestMapping("item-comments-api")
@FeignClient("item-comments-api")
public interface ItemCommentsApi {
    @PostMapping("saveItemComments")
    void saveComments(@RequestParam("userId") String userId,
                      @RequestParam("orderId") String orderId,
                      @RequestBody List<CenterCommentBO> comments);

    @GetMapping("myComments")
    PagedGridResult queryUserComment(@RequestParam("userId") String userId,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize);
}
