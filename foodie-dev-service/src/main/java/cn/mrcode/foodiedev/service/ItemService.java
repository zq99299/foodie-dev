package cn.mrcode.foodiedev.service;


import cn.mrcode.foodiedev.common.util.PagedGridResult;
import cn.mrcode.foodiedev.pojo.Items;
import cn.mrcode.foodiedev.pojo.ItemsImg;
import cn.mrcode.foodiedev.pojo.ItemsParam;
import cn.mrcode.foodiedev.pojo.ItemsSpec;
import cn.mrcode.foodiedev.pojo.vo.CommentLevelCountsVO;
import cn.mrcode.foodiedev.pojo.vo.ShopcartVO;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品 ID 查询详情
     *
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品 id 查询商品图片列表
     *
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品 id 查询商品规格
     *
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品  id 查询商品参数
     *
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询商品的评价等级数量
     *
     * @param itemId
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id查询商品的评价（分页）
     *
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level,
                                       Integer page, Integer pageSize);

    /**
     * 搜索商品列表（分页）
     *
     * @param keywords 搜索词
     * @param sort     排序
     * @return
     */
    PagedGridResult searchItems(String keywords, String sort,
                                Integer page, Integer pageSize);

    /**
     * 搜索商品列表，分类搜索（分页）
     *
     * @param catId 搜索词
     * @param sort  排序
     * @return
     */
    PagedGridResult searchItemsByThirdCat(Integer catId, String sort,
                                          Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新的购物车中商品数据（用于刷新渲染购物车中的商品数据）
     *
     * @param specIds
     * @return
     */
    List<ShopcartVO> queryItemsBySpecIds(String specIds);
}
