package com.tristeza.distributedlocks.zk.task;

import com.tristeza.distributedlocks.zk.utils.ZKLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2020/7/16 5:36 下午
 */
@Service
public class ZookeeperDistributeTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperDistributeTask.class);

//    @Scheduled(cron = "0/5 * * * * ?")
    public void task() {
        try (ZKLock lock = new ZKLock()) {
            if (lock.getLock("order")) {
                LOGGER.info("获得锁");
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
