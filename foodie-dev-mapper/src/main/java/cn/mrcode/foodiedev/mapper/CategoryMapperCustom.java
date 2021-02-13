package cn.mrcode.foodiedev.mapper;

import cn.mrcode.foodiedev.pojo.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
