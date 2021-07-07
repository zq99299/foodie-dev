package cn.mrcode.foodiedev.api.controller;

import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.common.util.JsonUtils;
import cn.mrcode.foodiedev.common.util.RedisOperator;
import cn.mrcode.foodiedev.pojo.bo.ShopcartBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "购物接口", tags = {"购物车接口相关的 api"})
@RestController
@RequestMapping("shopcart")
public class ShopcatController extends BaseController {
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        System.out.println(JsonUtils.objectToJson(shopcartBO));
        // 这里需要校验是否登录之类的，后续再来完善该代码
        // 这里只简单的判定是否有传递 userId
        // 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到 redis 缓存

        // 1. 判断当前购物车中包含已经存在的商品，则将它的数量加 1
        //    这里的常量是 shopcart
        String key = BaseController.FOODIE_SHOPCART + ":" + userId;
        String shopcartJson = redisOperator.get(key);
        List<ShopcartBO> shopcartBOList = null;

        // redis 中已经有购物车
        if (StringUtils.isNotBlank(shopcartJson)) {
            shopcartBOList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            // 判断是否有重复的商品，如果有，则将购买数量累加
            boolean isHaving = false;
            for (ShopcartBO sc : shopcartBOList) {
                String specId = sc.getSpecId();
                if (specId.equals(shopcartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            // 如果不存在，则添加到购物车中
            if (!isHaving) {
                shopcartBOList.add(shopcartBO);
            }
        }
        // 如果没有购物车，直接添加到购物车中
        else {
            shopcartBOList = new ArrayList<>();
            shopcartBOList.add(shopcartBO);
        }

        // 最后覆盖掉缓存中的购物车数据
        redisOperator.set(key, JsonUtils.objectToJson(shopcartBOList));
        return JSONResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public JSONResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }

        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品

        return JSONResult.ok();
    }
}
