package cn.mrcode.rabbit.task.annotation;

import cn.mrcode.rabbit.task.autoconfigure.JobParserAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author mrcode
 * @date 2021/11/25 20:41
 */
@Target(ElementType.TYPE) // 作用于类或接口上
@Retention(RetentionPolicy.RUNTIME) // 注解会被编译器记录在类文件中，并在运行时由 VM 保留，因此它们可以被反射读取
@Documented // 被 javadoc 类似工具记录
@Inherited  // 自动继承注解类型，如果该注解继承一个注解的话，可以继承超类的注解
@Import(JobParserAutoConfiguration.class)
public @interface EnableElasticJob {
}
