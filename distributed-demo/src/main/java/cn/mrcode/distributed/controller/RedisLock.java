package cn.mrcode.distributed.controller;

import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Arrays;
import java.util.UUID;

/**
 * Redis setnx 分布式锁工具封装
 *
 * @author mrcode
 * @date 2022/1/17 21:47
 */
public class RedisLock implements AutoCloseable {
    private RedisTemplate redisTemplate;
    private String key;
    private String value;
    private int expireTime;

    /**
     * @param redisTemplate
     * @param key           你的锁名称，不同业务可能锁不同
     * @param expireTime    锁过期时间，单位秒
     */
    public RedisLock(RedisTemplate redisTemplate, String key, int expireTime) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expireTime = expireTime;
        // value 可以不暴露出去，每个线程都是不一样的
        this.value = UUID.randomUUID().toString();
    }

    /**
     * 获取锁
     *
     * @return
     */
    public boolean getLock() {
        // 获取分布式锁
        // RedisCallback
        Boolean lock = (Boolean) redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Expiration expiration = Expiration.seconds(expireTime);
            // NX
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            // 由于这里需要接受 byte, 不能暴力的使用 string.getBytes()
            // 要使用模板里面的 key\value 序列化器来实现
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
            Boolean result = redisConnection.set(redisKey, redisValue, expiration, setOption);
            return result;
        });
        return lock;
    }

    /**
     * 释放锁
     *
     * @return
     */
    public boolean unLock() {
        // lua 脚本
        String script = "if redis.call(\"get\",KEYS[1])==ARGV[1] then\n" +
                "\treturn redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "\treturn 0\n" +
                "end";
        RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
        Boolean result = (Boolean) redisTemplate.execute(redisScript, Arrays.asList(key), value);
        return result;
    }

    @Override
    public void close() throws Exception {
        unLock();
    }
}
