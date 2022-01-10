package cn.mrcode.distributed.controller;

import cn.mrcode.distributed.repo.bean.DistributeLock;
import cn.mrcode.distributed.repo.mapper.DistributeLockExtMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mrcode
 * @date 2022/1/9 19:30
 */
@RestController
@Slf4j
public class DemoController {
    private Lock lock = new ReentrantLock();

    @RequestMapping("/singleLock")
    public String singleLock() throws InterruptedException {
        log.info("我进入了方法");
        lock.lock();
        log.info("我进入了锁");
        TimeUnit.SECONDS.sleep(60);
        lock.unlock();
        return "我已经执行完成！";
    }


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DistributeLockExtMapper distributeLockExtMapper;

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/singleLock-db")
    public String singleLockDb() throws InterruptedException {
        log.info("我进入了方法");
        DistributeLock dbLock = distributeLockExtMapper.select("demo");
        log.info("我进入了锁");
        TimeUnit.SECONDS.sleep(60);
        return "我已经执行完成！";
    }
}
