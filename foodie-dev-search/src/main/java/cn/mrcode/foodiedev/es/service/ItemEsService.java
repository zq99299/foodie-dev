package cn.mrcode.foodiedev.es.service;

import cn.mrcode.foodiedev.common.util.PagedGridResult;

/**
 * @author mrcode
 * @date 2021/8/30 22:23
 */
public interface ItemEsService {
    /**
     * 搜索商品列表（分页）
     *
     * @param keywords 搜索词
     * @param sort     排序
     * @return
     */
    PagedGridResult searchItems(String keywords, String sort,
                                Integer page, Integer pageSize);
}
