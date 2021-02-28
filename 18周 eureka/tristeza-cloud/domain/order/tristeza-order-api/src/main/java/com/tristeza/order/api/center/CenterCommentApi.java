package com.tristeza.order.api.center;

import com.tristeza.item.model.bo.CenterCommentBO;
import com.tristeza.order.model.pojo.OrderItems;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("center-order-comments-api")
public interface CenterCommentApi {
    @GetMapping("orderItems")
    List<OrderItems> queryPendingComments(@RequestParam("orderId") String orderId);

    @PostMapping("orderComments")
    void saveComments(@RequestParam("userId") String userId,
                      @RequestParam("orderId") String orderId,
                      @RequestBody List<CenterCommentBO> comments);
}
