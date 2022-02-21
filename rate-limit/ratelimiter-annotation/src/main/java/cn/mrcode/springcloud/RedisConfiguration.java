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
    /**
     * 业务方也需要依赖我们这个项目的 jar 包，然后配置 spring-data-redis 的自动配置
     *
     * @param factory 这里的 factory 就是自动配置产生的
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory factory
    ) {
        return new StringRedisTemplate(factory);
    }

    /**
     * 设置 Lua 脚本信息对象，用于在执行脚本时的引用
     *
     * @return
     */
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
