package cn.mrcode.foodiedev.api.controller;

import cn.mrcode.foodiedev.common.enums.YesOrNo;
import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.pojo.Carousel;
import cn.mrcode.foodiedev.service.CarouselService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mrcode
 * @date 2021/2/13 21:44
 */
@Api(value = "首页", tags = {"首页相关接口"})
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;

    @ApiOperation(value = "获取首页轮播图列表")
    @GetMapping("/carousel")
    public JSONResult carousel() {
        List<Carousel> carousels = carouselService.queryAll(YesOrNo.YES.type);
        return JSONResult.ok(carousels);
    }
}
