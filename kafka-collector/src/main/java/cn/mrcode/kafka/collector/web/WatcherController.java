package cn.mrcode.kafka.collector.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mrcode
 * @date 2022/1/7 20:43
 */
@RestController
public class WatcherController {
    @RequestMapping("/accurateWatch")
    public String watch(@RequestBody WatcherItem params) {
        System.out.println("watcher: " + params);
        return "OK";
    }
}
