package com.imooc;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//打包war [4] 增加启动类
public class WarStartApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    	// 指向Application这个spingboot的启动类
        return builder.sources(Application.class);
    }
}