package cn.mrcode.distributed.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author mrcode
 * @date 2022/1/20 20:47
 */
@Slf4j
public class DemoLockTest {
    @Test
    public void testCuratorLock() {
        // 初始化客户端
        String zookeeperConnectionString = "127.0.0.1:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();


        String lockPath = "/order";
        InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        // 获取锁，最多等待 30 秒
        try {
            if (lock.acquire(30, TimeUnit.SECONDS)) {
                try {
                    log.info("我获得了锁");
                } finally {
                    lock.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
