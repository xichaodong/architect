package com.tristeza.item.remote;

import com.tristeza.cloud.model.pojo.PagedGridResult;
import com.tristeza.item.api.ItemCommentsApi;
import com.tristeza.item.model.bo.CenterCommentBO;
import com.tristeza.service.ItemCommentsService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chaodong.xi
 * @date 2021/2/24 11:55 上午
 */
@RestController
public class ItemCommentsApiImpl implements ItemCommentsApi {
    @Resource
    private ItemCommentsService itemCommentsService;

    @Override
    public void saveComments(String userId, String orderId, List<CenterCommentBO> comments) {
        itemCommentsService.saveComments(userId, orderId, comments);
    }

    @Override
    public PagedGridResult queryUserComment(String userId, Integer page, Integer pageSize) {
        return itemCommentsService.queryUserComment(userId, page, pageSize);
    }
}
