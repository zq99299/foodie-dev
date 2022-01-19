package cn.mrcode.distributed.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redis setnx 分布式锁实现
 *
 * @author mrcode
 * @date 2022/1/17 20:54
 */
@RestController
@Slf4j
public class RedisLockController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/redisLock")
    public String redisLock() {
        log.info("我进入了方法");
        final String key = "redisKey";
        // 每个线程的 KEY 不一致，因此使用 UUID
        String value = UUID.randomUUID().toString();

        // 获取分布式锁
        // RedisCallback
        Boolean lock = (Boolean) redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Expiration expiration = Expiration.seconds(30);
            // NX
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            // 由于这里需要接受 byte, 不能暴力的使用 string.getBytes()
            // 要使用模板里面的 key\value 序列化器来实现
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
            Boolean result = redisConnection.set(redisKey, redisValue, expiration, setOption);
            return result;
        });
        // 如果返回结果是 true ,则表示获取到了锁
        if (lock) {
            log.info("我进入了锁！！");
            try {
                // 模拟执行业务耗时
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 执行万业务后，释放锁
                // lua 脚本
                String script = "if redis.call(\"get\",KEYS[1])==ARGV[1] then\n" +
                        "\treturn redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "\treturn 0\n" +
                        "end";
                RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
                Boolean result = (Boolean) redisTemplate.execute(redisScript, Arrays.asList(key), value);
                log.info("释放锁结果：{}", result);
            }
        }
        log.info("方法完成");
        return "ok";
    }

    @RequestMapping("/redisLock-2")
    public String redisLock2() {
        log.info("我进入了方法");
        final String key = "redisKey";

        RedisLock redisLock = new RedisLock(redisTemplate, key, 30);
        // 如果返回结果是 true ,则表示获取到了锁
        if (redisLock.getLock()) {
            log.info("我进入了锁！！");
            try {
                // 模拟执行业务耗时
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Boolean result = redisLock.unLock();
                log.info("释放锁结果：{}", result);
            }
        }
        log.info("方法完成");
        return "ok";
    }


    /**
     * 使用自动释放锁功能
     *
     * @return
     */
    @RequestMapping("/redisLock-3")
    public String redisLock3() {
        log.info("我进入了方法");
        final String key = "redisKey";

        // try-with-resources 语句
        try (
                RedisLock redisLock = new RedisLock(redisTemplate, key, 30);
        ) {
            // 如果返回结果是 true ,则表示获取到了锁
            if (redisLock.getLock()) {
                log.info("我进入了锁！！");
                // 模拟执行业务耗时
                TimeUnit.SECONDS.sleep(15);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        log.info("方法完成");
        return "ok";
    }
}
