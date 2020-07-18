package com.tristeza.distributedlocks.zk.utils;

import com.tristeza.distributedlocks.redis.controller.RedisLockController;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author chaodong.xi
 * @date 2020/7/16 6:39 下午
 */
public class ZKLock implements AutoCloseable, Watcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockController.class);

    private ZooKeeper zooKeeper;

    private String zkNode;

    public ZKLock() throws IOException {
        this.zooKeeper = new ZooKeeper("172.16.16.60:2181", 10000, this);
    }

    public boolean getLock(String bizCode) {
        try {
            Stat stat = zooKeeper.exists(String.format("/%s", bizCode), false);
            if (Objects.isNull(stat)) {
                zooKeeper.create(String.format("/%s", bizCode), bizCode.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zkNode = zooKeeper.create(String.format("/%s/%s_", bizCode, bizCode), bizCode.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            List<String> childrenNodes = zooKeeper.getChildren(String.format("/%s", bizCode), false);

            Collections.sort(childrenNodes);

            String firstNode = childrenNodes.get(0);

            if (zkNode.endsWith(firstNode)) {
                return true;
            }

            String lastNode = firstNode;

            for (String node : childrenNodes) {
                if (zkNode.endsWith(node)) {
                    zooKeeper.exists(String.format("/%s/%s", bizCode, lastNode), true);
                    break;
                } else {
                    lastNode = node;
                }
            }

            synchronized (this) {
                wait();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        zooKeeper.delete(zkNode, -1);
        zooKeeper.close();

        LOGGER.info("锁已经释放!");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.EventType.NodeDeleted == watchedEvent.getType()) {
            synchronized (this) {
                notify();
            }
        }
    }
}
