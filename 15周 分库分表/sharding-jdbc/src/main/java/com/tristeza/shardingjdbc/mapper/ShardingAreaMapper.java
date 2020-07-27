package com.tristeza.shardingjdbc.mapper;

import com.tristeza.shardingjdbc.model.ShardingArea;
import com.tristeza.shardingjdbc.model.ShardingOrder;

import java.util.List;

public interface ShardingAreaMapper {
    List<ShardingArea> loadAllSharingArea();

    Long insertShardingArea(ShardingArea area);
}