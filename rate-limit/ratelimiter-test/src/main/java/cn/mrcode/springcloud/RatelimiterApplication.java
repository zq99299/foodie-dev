package cn.mrcode.springcloud;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author mrcode
 * @date 2022/2/21 20:42
 */
@SpringBootApplication
public class RatelimiterApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(RatelimiterApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
