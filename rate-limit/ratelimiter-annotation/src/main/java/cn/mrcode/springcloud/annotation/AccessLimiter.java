package cn.mrcode.springcloud.annotation;

import java.lang.annotation.*;

/**
 * @author mrcode
 * @date 2022/2/22 19:48
 */
@Target(ElementType.METHOD) // 作用于方法基本
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimiter {
    int limit();

    String methodKey() default "";
}
