package com.tristeza.distributedlocks;

import com.tristeza.distributedlocks.mysql.mapper.DistributeLockMapper;
import com.tristeza.distributedlocks.mysql.model.DistributeLock;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class DistributedLocksApplicationTests {
    @Resource
    private DistributeLockMapper distributeLockMapper;

    @Test
    void contextLoads() {
        List<DistributeLock> distributeLocks = distributeLockMapper.loadAllDistributeLocks();
        System.out.println(distributeLocks.get(0).toString());
    }
}
