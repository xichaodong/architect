package com.tristeza.shardingjdbc.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * @author chaodong.xi
 * @date 2020/7/29 8:41 下午
 */
public class UUIDSharding implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String id = preciseShardingValue.getValue();
        int mod = Math.abs(id.hashCode() % collection.size());

        return collection.toArray(new String[0])[mod];
    }
}
