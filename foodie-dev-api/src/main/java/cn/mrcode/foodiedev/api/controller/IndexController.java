package cn.mrcode.foodiedev.api.controller;

import cn.mrcode.foodiedev.common.enums.YesOrNo;
import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.common.util.JsonUtils;
import cn.mrcode.foodiedev.common.util.RedisOperator;
import cn.mrcode.foodiedev.pojo.Carousel;
import cn.mrcode.foodiedev.pojo.Category;
import cn.mrcode.foodiedev.pojo.vo.CategoryVO;
import cn.mrcode.foodiedev.pojo.vo.NewItemsVo;
import cn.mrcode.foodiedev.service.CarouselService;
import cn.mrcode.foodiedev.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表")
    @GetMapping("/carousel")
    public JSONResult carousel() {
        String carouselStr = redisOperator.get("carousel");
        List<Carousel> carousels = null;

        // 如果 redis 中没有，则去数据库中获取
        if (StringUtils.isBlank(carouselStr)) {
            carousels = carouselService.queryAll(YesOrNo.YES.type);
            // 并且将数据缓存到 redis 中
            redisOperator.set("carousel", JsonUtils.objectToJson(carousels));
        } else {
            // 否则，直接从反序列化
            carousels = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }

        return JSONResult.ok(carousels);
    }

    @ApiOperation(value = "获取商品一级分类")
    @GetMapping("/cats")
    public JSONResult cats() {
        List<Category> list = null;
        String catStr = redisOperator.get("cat");
        if (StringUtils.isBlank(catStr)) {
            list = categoryService.queryAllRootLeveCat();
            redisOperator.set("cat", JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(catStr, Category.class);
        }

        return JSONResult.ok(list);
    }

    @ApiOperation(value = "获取商品子级分类")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类 ID", required = true)
            @PathVariable Integer rootCatId) {
        List<CategoryVO> list = null;
        String subCatStr = redisOperator.get("subCat:" + rootCatId);
        if (StringUtils.isBlank(subCatStr)) {
            list = categoryService.getSubCatList(rootCatId);
            if (list != null && list.size() > 0) {
                // 为空的时候，设置缓存时间为 5 分钟
                redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(list), 60 * 5);
            } else {
                redisOperator.set("subCat:" + rootCatId, JsonUtils.objectToJson(list));
            }
        } else {
            list = JsonUtils.jsonToList(subCatStr, CategoryVO.class);
        }
        return JSONResult.ok(list);
    }

    @ApiOperation(value = "获取一级分类下的最新 6 个商品")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类 ID", required = true)
            @PathVariable Integer rootCatId) {
        List<NewItemsVo> list = categoryService.getSixNewItemsLazy(rootCatId);
        return JSONResult.ok(list);
    }
}
