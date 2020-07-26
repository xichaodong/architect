package com.tristeza.shardingjdbc.mapper;

import com.tristeza.shardingjdbc.model.ShardingOrder;

import java.util.List;

public interface ShardingOrderMapper {
    List<ShardingOrder> loadAllSharingOrder();
}