package cn.mrcode.foodiedev.sso;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SSOController {

    @GetMapping("/hello")
    @ResponseBody
    public Object hello() {
        return "Hello Word";
    }
}
