package cn.mrcode.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mrcode
 * @date 2022/2/21 20:43
 */
@RestController
@Slf4j
public class TestController {
    @Autowired
    private AccessLimiterService accessLimiterService;

    @GetMapping("/test")
    public String test() {
        // 调用限流
        try {
            accessLimiterService.limitAccess("ratelimiter-test", 3);
        } catch (Exception e) {
            return e.getMessage();
        }
        log.info("获得执行");
        return "success";
    }
}
