package cn.mrcode.foodiedev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author mrcode
 * @date 2021/2/16 23:33
 */
@Configuration
public class WebMvcConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
