package cn.mrcode.foodiedev.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
public class HelloController {
    @GetMapping("/hello")
    public Object hello() {
        return "Hello Word";
    }
}
