package cn.mrcode.distributed.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author mrcode
 * @date 2022/1/19 21:13
 */
@RestController
@Slf4j
public class ZookeeperLockController {

    @RequestMapping("/zkLock")
    public String zkLock() {
        log.info("我进入了方法！！");
        try (
                ZkLock zkLock = new ZkLock("127.0.0.1:2181", 10000);
        ) {
            if (zkLock.getLock("order")) {
                log.info("我进入了锁！！");
                try {
                    // 模拟执行业务耗时
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("方法完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
