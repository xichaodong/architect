package com.tristeza.distributedlocks;

import com.tristeza.distributedlocks.mysql.mapper.DistributeLockMapper;
import com.tristeza.distributedlocks.mysql.model.DistributeLock;
import com.tristeza.distributedlocks.redis.controller.RedisLockController;
import com.tristeza.distributedlocks.zk.utils.ZKLock;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class DistributedLocksApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLocksApplicationTests.class);

    @Resource
    private DistributeLockMapper distributeLockMapper;

    @Test
    void contextLoads() throws IOException {
        ZKLock lock = new ZKLock();

        boolean result = lock.getLock("order");

        LOGGER.info("获得锁的结果:{}", result);
    }
}
