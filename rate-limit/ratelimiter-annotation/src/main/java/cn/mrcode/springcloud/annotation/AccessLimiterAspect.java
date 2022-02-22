package cn.mrcode.springcloud.annotation;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author mrcode
 * @date 2022/2/22 19:50
 */
@Aspect
@Slf4j
public class AccessLimiterAspect {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisScript<Boolean> rateLimitLua;

    @Pointcut("@annotation(cn.mrcode.springcloud.annotation.AccessLimiter)")
    public void cut() {
        log.info("cut");
    }

    //  @Before("cut()")  // 或则 引用上面的定义的切入点
    // 直接写表达式也可以
    @Before("@annotation(cn.mrcode.springcloud.annotation.AccessLimiter)")
    public void before(JoinPoint point) {
        // 获取到这个方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        final AccessLimiter annotation = method.getAnnotation(AccessLimiter.class);
        if (annotation == null) {
            return;
        }

        String key = annotation.methodKey();
        // 如果没有，则生成一个
        // 我们这里使用这个方法签名+参数名来生成
        if (StringUtils.isEmpty(key)) {
            // 获取方法的参数类型比如[java.util.String]
            Class<?>[] parameterTypes = method.getParameterTypes();
            //方法名称,如：testAnnotation
            key = method.getName();
            if (parameterTypes != null) {
                String paramTypes = Arrays.stream(parameterTypes)
                        .map(Class::getName)
                        .collect(Collectors.joining(","));
                key += "#" + paramTypes;
            }
        }
        log.info("key:{}", key);
        int limit = annotation.limit();
        boolean acquired = stringRedisTemplate.execute(
                rateLimitLua, // lua 脚本
                Lists.newArrayList(key),  // Lua 脚本中的 key 列表
                limit + ""  // Lua 脚本中的 value 列表
        );

        if (!acquired) {
            // 访问被阻止
            log.error("your access is blocked , key={}", key);
            throw new RuntimeException("Your access is blocked");
        }
    }
}
