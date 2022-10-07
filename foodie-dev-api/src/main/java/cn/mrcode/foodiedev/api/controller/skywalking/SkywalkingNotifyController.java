package cn.mrcode.foodiedev.api.controller.skywalking;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mrcode
 * @date 2022/10/7 14:23
 */
@RestController
@RequestMapping("/skywalking")
public class SkywalkingNotifyController {
    @PostMapping("/notify")
    public void notify(@RequestBody List<SkywalkingNotify> alarms) {
        System.out.println("收到告警信息：" + alarms);
    }
}
