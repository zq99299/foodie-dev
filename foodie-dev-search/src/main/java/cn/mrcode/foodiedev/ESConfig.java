package cn.mrcode.foodiedev;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author mrcode
 * @date 2021/8/25 22:26
 */
@Configuration
public class ESConfig {
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
