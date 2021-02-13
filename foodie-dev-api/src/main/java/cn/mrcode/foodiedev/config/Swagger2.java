package cn.mrcode.foodiedev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author mrcode
 * @date 2021/2/13 13:06
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    // 原始 API 文档：  http://localhost:8088/swagger-ui.html
    // 第三方 API 文档：http://localhost:8088/doc.html
    // 核心配置 docket
    @Bean
    public Docket createTestApi() {
        return new Docket(DocumentationType.SWAGGER_2) // 指定 API 类型为 2
                .apiInfo(apiInfo())                    // 定义 API 文档汇总信息
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("cn.mrcode.foodiedev.api.controller")) // 指定 controller 包
                .paths(PathSelectors.any())  // 该包中的所有 controller
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台接口 API")   // 标题
                .contact(new Contact("mrcode", "mrcode.cn", "api@mrcode.cn")) // 联系人信息
                .description("天天吃货 API 文档")   // 详细信息
                .version("1.0.1") // 文档版本号
                .termsOfServiceUrl("http://mrcode.cn") // 网站地址
                .build();
    }
}
