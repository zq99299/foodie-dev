package cn.mrcode.sharding.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mrcode
 * @date 2022/1/9 19:30
 */
@RestController
@Slf4j
public class DemoController {

    @RequestMapping("/demo")
    public String singleLock() throws InterruptedException {
        return "我已经执行完成！";
    }
}
