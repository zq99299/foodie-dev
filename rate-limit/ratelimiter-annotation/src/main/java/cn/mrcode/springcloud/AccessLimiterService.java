package cn.mrcode.springcloud;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

/**
 * @author mrcode
 * @date 2022/2/21 20:18
 */
@Service
@Slf4j
public class AccessLimiterService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisScript<Boolean> rateLimitLua;

    /**
     * @param key   分业务模块限流，这个可以自定义
     * @param limit 一秒钟内最多可以通过多少请求
     */
    public void limitAccess(String key, Integer limit) {
        boolean acquired = stringRedisTemplate.execute(
                rateLimitLua, // lua 脚本
                Lists.newArrayList(key),  // Lua 脚本中的 key 列表
                limit.toString()  // Lua 脚本中的 value 列表
        );

        if (!acquired) {
            // 访问被阻止
            log.error("your access is blocked , key={}", key);
            throw new RuntimeException("Your access is blocked");
        }
    }
}
