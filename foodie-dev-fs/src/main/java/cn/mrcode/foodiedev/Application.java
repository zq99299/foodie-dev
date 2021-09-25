package cn.mrcode.foodiedev;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(
        scanBasePackages = {"cn.mrcode.foodiedev", "org.n3r.idworker"},
        exclude = {SecurityAutoConfiguration.class}
)
@MapperScan("cn.mrcode.foodiedev.mapper")
@EnableRedisHttpSession
@EnableScheduling
@ConfigurationPropertiesScan({"cn.mrcode.foodiedev.fs.config"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
