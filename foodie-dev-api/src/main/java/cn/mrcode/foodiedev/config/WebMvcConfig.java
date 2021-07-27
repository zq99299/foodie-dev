package cn.mrcode.foodiedev.config;

import cn.mrcode.foodiedev.interceptor.UserTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mrcode
 * @date 2021/2/16 23:33
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                // 这里拦截之后，之前能访问的 swagger2 文档就无法访问到了
                // 因为它也是第三方 jar 包提供的 html 静态文件，所以这里也需要映射下
                .addResourceLocations("classpath:/META-INF/resources/")  // 映射 swagger2
                .addResourceLocations("file:/Users/mrcode/Documents/GitHub/foodie-dev/tempresource/images/");  // 映射本地静态资源
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userTokenInterceptor())
                // 拦截的路径
                .addPathPatterns("/hello")
                .addPathPatterns("/shopcart/add")
                .addPathPatterns("/shopcart/del")
                .addPathPatterns("/address/list")
                .addPathPatterns("/address/add")
                .addPathPatterns("/address/update")
                .addPathPatterns("/address/setDefalut")
                .addPathPatterns("/address/delete")
                .addPathPatterns("/orders/*")
                .addPathPatterns("/center/*")
                .addPathPatterns("/userInfo/*")
                .addPathPatterns("/myorders/*")
                .addPathPatterns("/mycomments/*")
                // 不拦截的路径：由于上面有的使用了通配符 * ，下面这里会在上面的拦截范围中
                //      但是我们又不想要拦截这些路径
                .excludePathPatterns("/myorders/deliver")
                .excludePathPatterns("/orders/notifyMerchantorderPaid")
        ;
        // WebMvcConfigurer.super.addInterceptors(registry);
    }
}
