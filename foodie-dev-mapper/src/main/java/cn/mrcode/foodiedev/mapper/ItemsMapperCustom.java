package cn.mrcode.foodiedev.mapper;

import cn.mrcode.foodiedev.pojo.vo.ItemCommentVO;
import cn.mrcode.foodiedev.pojo.vo.SearchItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author mrcode
 * @date 2021/2/15 15:40
 */
public interface ItemsMapperCustom {
    List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

    List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);

    List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);
}
