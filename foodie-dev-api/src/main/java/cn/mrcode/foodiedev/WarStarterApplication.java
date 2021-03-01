package cn.mrcode.foodiedev;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * war 包启动4: 启动类
 *
 * @author mrcode
 * @date 2021/2/28 21:40
 */
public class WarStarterApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 指向之前的启动类
        return builder.sources(Application.class);
    }
}
