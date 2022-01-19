package cn.mrcode.distributed.service;

import cn.mrcode.distributed.controller.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 演示定时任务重复执行
 *
 * @author mrcode
 * @date 2022/1/17 22:14
 */
@Service
@Slf4j
public class SchedulerService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 5 秒发送一次
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void sendSms() {
        try (
                RedisLock redisLock = new RedisLock(redisTemplate, "autoSendSms", 30);
        ) {
            if (redisLock.getLock()) {
                log.info("向 xxxxxxxx 发送短信");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
