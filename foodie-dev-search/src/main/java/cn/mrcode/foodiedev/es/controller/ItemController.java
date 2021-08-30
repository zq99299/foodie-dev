package cn.mrcode.foodiedev.es.controller;

import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.common.util.PagedGridResult;
import cn.mrcode.foodiedev.es.service.ItemEsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemController {
    @Autowired
    private ItemEsService itemEsService;

    @GetMapping("/hello")
    public Object hello() {
        return "Hello Word";
    }

    /**
     * 替代的接口是：cn.mrcode.foodiedev.api.controller.ItemsController.search
     *
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public JSONResult search(
            @ApiParam(name = "keywords", value = "关键词", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序,        " +
                    "        -- k: 默认，根据 name\n" +
                    "        -- c: 根据销售数量排序\n" +
                    "        -- p: 根据价格排序", required = false)
            @RequestParam(required = false) String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam(required = false) Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam(required = false) Integer pageSize) {

        if (page == null) {
            page = 1;
        }
        // es 分页从 0 开始
        page--;

        if (pageSize == null) {
            pageSize = 20;
        }

        PagedGridResult grid = itemEsService.searchItems(keywords,
                sort,
                page,
                pageSize);

        return JSONResult.ok(grid);
    }
}
