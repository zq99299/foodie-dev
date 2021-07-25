package cn.mrcode.foodiedev.api.controller;

import cn.mrcode.foodiedev.common.util.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mrcode
 * @date 2021/6/30 22:39
 */
@ApiIgnore
@RestController
@RequestMapping("redis")
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key, String value) {
        // opsForxxx 里面很好多是我们之前讲解过的 redis 数据类型对应的
        // redisTemplate.opsForValue().set(key, value);
        redisOperator.set(key, value);
        return "OK";
    }

    @GetMapping("/get")
    public Object get(String key) {
        // return redisTemplate.opsForValue().get(key);
        return redisOperator.get(key);
    }

    @GetMapping("/delete")
    public Object delete(String key) {
        // 删除 key 是通用操作
        // redisTemplate.delete(key);
        redisOperator.del(key);
        return "OK";
    }

    @GetMapping("/getALot")
    public Object getALot(String... keys) {
        List<Object> result = redisOperator.batchGet(Arrays.asList(keys));
        return result;
    }
}
