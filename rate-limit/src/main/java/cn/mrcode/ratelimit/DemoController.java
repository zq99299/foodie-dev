package cn.mrcode.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author mrcode
 * @date 2022/2/16 21:01
 */
@RestController
@Slf4j
public class DemoController {
    // 每秒释放 2 个令牌
    RateLimiter limiter = RateLimiter.create(2.0);

    /**
     * 非阻塞限流
     */
    @GetMapping("/tryAcquire")
    public String tryAcquire(Integer count) {
        // tryAcquire 默认是消耗 1 个令牌，可以传递参数这次消耗 n 个令牌
        if (limiter.tryAcquire(count)) {
            log.info("success, 当前的稳定速率是 {}", limiter.getRate());
            return "success";
        }

        log.info("fail, 当前的稳定速率是 {}", limiter.getRate());
        return "fail";
    }

    /**
     * 限定时间的：非阻塞限流
     */
    @GetMapping("/tryAcquireWithTimout")
    public String tryAcquireWithTimout(Integer count, Integer timeout) {
        // tryAcquire 默认是消耗 1 个令牌，可以传递参数这次消耗 n 个令牌
        // 如果超时后 还未获得令牌，则返回 false
        if (limiter.tryAcquire(count, timeout, TimeUnit.SECONDS)) {
            log.info("success, 当前的稳定速率是 {}", limiter.getRate());
            return "success";
        }

        log.info("fail, 当前的稳定速率是 {}", limiter.getRate());
        return "fail";
    }


    /**
     * 同步阻塞限流
     */
    @GetMapping("/acquire")
    public String acquire(Integer count) {
        limiter.acquire(count);
        log.info("success, 当前的稳定速率是 {}", limiter.getRate());
        return "success";
    }

    /**
     * Nginx 专用
     */
    @GetMapping("/nginx")
    public String nginx() {
        log.info("Nginx success");
        return "success";
    }
}
