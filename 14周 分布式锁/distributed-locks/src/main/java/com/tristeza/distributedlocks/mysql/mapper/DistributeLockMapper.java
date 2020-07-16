package com.tristeza.distributedlocks.mysql.mapper;

import com.tristeza.distributedlocks.mysql.model.DistributeLock;

import java.util.List;

public interface DistributeLockMapper {
    List<DistributeLock> loadAllDistributeLocks();
}