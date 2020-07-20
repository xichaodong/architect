package com.tristeza.distributedlocks.zk.task;

import com.tristeza.distributedlocks.zk.utils.ZKLock;
import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2020/7/16 5:36 下午
 */
@Service
public class ZookeeperDistributeTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperDistributeTask.class);

    @Resource
    private CuratorFramework curatorFramework;

    //    @Scheduled(cron = "0/5 * * * * ?")
    public void commonTask() {
        try (ZKLock lock = new ZKLock()) {
            if (lock.getLock("order")) {
                LOGGER.info("获得锁");
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Scheduled(cron = "0/5 * * * * ?")
    public void curatorTask() throws Exception {
        LOGGER.info("进入方法");
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, "/order2");
        if (lock.acquire(30, TimeUnit.SECONDS)) {
            try {
                LOGGER.info("获得锁");
                TimeUnit.SECONDS.sleep(5);
            } finally {
                lock.release();
                LOGGER.info("方法执行完毕");
            }
        }
    }
}
