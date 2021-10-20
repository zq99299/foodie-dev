package cn.mrcode.rabbit.producer.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置类入口
 *
 * @author mrcode
 * @date 2021/10/20 21:51
 */
@Configuration
@ComponentScan({"cn.mrcode.rabbit.producer.*"})
public class RabbitProducerAutoConfiguration {
}