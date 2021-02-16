package cn.mrcode.foodiedev.api.controller;

import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.common.util.PagedGridResult;
import cn.mrcode.foodiedev.pojo.Items;
import cn.mrcode.foodiedev.pojo.ItemsImg;
import cn.mrcode.foodiedev.pojo.ItemsParam;
import cn.mrcode.foodiedev.pojo.ItemsSpec;
import cn.mrcode.foodiedev.pojo.vo.CommentLevelCountsVO;
import cn.mrcode.foodiedev.pojo.vo.ItemInfoVO;
import cn.mrcode.foodiedev.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {


    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONResult info(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg(null);
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return JSONResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品 id", required = true)
            @RequestParam String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg(null);
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return JSONResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public JSONResult comments(
            @ApiParam(name = "itemId", value = "商品 id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            // 继承了 BaseController 中定义的评论默认分页数量
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = itemService.queryPagedComments(itemId,
                level,
                page,
                pageSize);

        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public JSONResult search(
            @ApiParam(name = "keywords", value = "关键词", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序,        " +
                    "        -- k: 默认，根据 name\n" +
                    "        -- c: 根据效率排序\n" +
                    "        -- p: 根据价格排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            // 继承了 BaseController 中定义的评论默认分页数量
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItems(keywords,
                sort,
                page,
                pageSize);

        return JSONResult.ok(grid);
    }

    @ApiOperation(value = "搜索商品列表-按分类ID", notes = "按分类 ID 搜素商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public JSONResult catItems(
            @ApiParam(name = "catId", value = "三级分类 ID", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序,        " +
                    "        -- k: 默认，根据 name\n" +
                    "        -- c: 根据效率排序\n" +
                    "        -- p: 根据价格排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItemsByThirdCat(catId,
                sort,
                page,
                pageSize);

        return JSONResult.ok(grid);
    }
}
