package cn.mrcode.foodiedev.sso;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(
        scanBasePackages = {"cn.mrcode.foodiedev", "org.n3r.idworker"},
        exclude = {SecurityAutoConfiguration.class}
)
@MapperScan("cn.mrcode.foodiedev.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
