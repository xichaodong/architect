package com.tristeza.shardingjdbc.controller;

import com.tristeza.shardingjdbc.mapper.ShardingAreaMapper;
import com.tristeza.shardingjdbc.mapper.ShardingOrderMapper;
import com.tristeza.shardingjdbc.model.ShardingArea;
import com.tristeza.shardingjdbc.model.ShardingOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author chaodong.xi
 * @date 2020/7/15 8:50 下午
 */
@RestController
public class ShardingController {
    @Resource
    private ShardingOrderMapper shardingOrderMapper;
    @Resource
    private ShardingAreaMapper shardingAreaMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @GetMapping("sharding")
    public List<ShardingOrder> sharding() {
        return shardingOrderMapper.loadAllSharingOrder();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @GetMapping("sharding/add")
    public Long shardingOrderAdd(@RequestParam("userId") Integer userId,
                                 @RequestParam("orderId") String orderId) {
        ShardingOrder order = new ShardingOrder();

//        order.setId(orderId);
        order.setUserId(userId);
        order.setOrderAmount(BigDecimal.valueOf(10.0));
        order.setOrderStatus(1);

        return shardingOrderMapper.insertShardingOrder(order);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @GetMapping("sharding/area/add")
    public Long shardingAreaAdd(@RequestParam("id") Integer id,
                                @RequestParam("name") String name) {
        ShardingArea area = new ShardingArea();

        area.setId(id);
        area.setName(name);

        return shardingAreaMapper.insertShardingArea(area);
    }
}
