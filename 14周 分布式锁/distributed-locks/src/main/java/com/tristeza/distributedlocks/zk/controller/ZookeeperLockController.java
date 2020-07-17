package com.tristeza.distributedlocks.zk.controller;

import com.tristeza.distributedlocks.redis.utils.RedisLock;
import com.tristeza.distributedlocks.zk.utils.ZKLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2020/7/16 11:33 上午
 */
@RestController
public class ZookeeperLockController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperLockController.class);

    @GetMapping("zk/lock")
    public String lock() {
        LOGGER.info("我进入了方法");

        try (ZKLock lock = new ZKLock()) {
            if (lock.getLock("order1")) {
                LOGGER.info("我进入了锁");
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.info("方法执行完成");
        return "success";
    }
}
