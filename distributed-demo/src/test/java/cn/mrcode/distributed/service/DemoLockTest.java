package cn.mrcode.distributed.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

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

    @Test
    public void testRedissonLock() {
        // 构建客户端
        Config config = new Config();
        config
                // 由于是单节点，所以使用单节点的 API
                // 里面还包含哨兵、集群等选择可配置
                .useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
        ;
        RedissonClient redisson = Redisson.create(config);

        // 使用客户端
        RLock lock = redisson.getLock("order");
        // 锁超时时间，如果获取不到会阻塞等待获取到锁
        // 可以传递 -1，那么就意味着该锁没有超时时间
        lock.lock(30, TimeUnit.SECONDS);
        log.info("获得了锁");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("释放了锁");
            lock.unlock();
        }
    }
}
