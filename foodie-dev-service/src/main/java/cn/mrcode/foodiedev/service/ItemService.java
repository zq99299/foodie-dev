package cn.mrcode.foodiedev.service;


import cn.mrcode.foodiedev.pojo.Items;
import cn.mrcode.foodiedev.pojo.ItemsImg;
import cn.mrcode.foodiedev.pojo.ItemsParam;
import cn.mrcode.foodiedev.pojo.ItemsSpec;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品 ID 查询详情
     *
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品 id 查询商品图片列表
     *
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品 id 查询商品规格
     *
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品  id 查询商品参数
     *
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);
}
