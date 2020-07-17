package com.tristeza.distributedlocks.mysql.controller;

import com.tristeza.distributedlocks.mysql.mapper.DistributeLockMapper;
import com.tristeza.distributedlocks.mysql.model.DistributeLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2020/7/15 8:50 下午
 */
@RestController
public class MysqlLockController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlLockController.class);

    @Resource
    private DistributeLockMapper distributeLockMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @GetMapping("mysql/lock")
    public String singleLock() {
        LOGGER.info("我进入了方法");

        List<DistributeLock> distributeLocks = distributeLockMapper.loadAllDistributeLocks();

        if (CollectionUtils.isEmpty(distributeLocks)) {
            throw new RuntimeException("找不到锁");
        }

        LOGGER.info("我进入了锁");

        try {
            TimeUnit.SECONDS.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
