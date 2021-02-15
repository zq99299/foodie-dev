package cn.mrcode.foodiedev.mapper;

import cn.mrcode.foodiedev.pojo.vo.CategoryVO;
import cn.mrcode.foodiedev.pojo.vo.NewItemsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author mrcode
 * @date 2021/2/13 22:25
 */
public interface CategoryMapperCustom {
    /**
     * 获取子级分类
     *
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(@Param("rootCatId") Integer rootCatId);

    /**
     * 获取一级分类下最新的 6 条商品数据
     *
     * @param map 使用对象参数的形式传参
     * @return
     */
    List<NewItemsVo> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}
