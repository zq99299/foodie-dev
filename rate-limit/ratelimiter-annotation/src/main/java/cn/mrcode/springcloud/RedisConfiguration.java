package cn.mrcode.springcloud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

/**
 * @author mrcode
 * @date 2022/2/21 20:26
 */
@Configuration
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory factory
    ) {
        return new StringRedisTemplate(factory);
    }

    @Bean
    public DefaultRedisScript loadRedisScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript();
        // 设置 lua 脚本文件路径
        redisScript.setLocation(new ClassPathResource("ratelimiter.lua"));
        // 设置脚本返回值类型
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }
}
