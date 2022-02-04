package cn.mrcode.sharding.repo.mapper;

import cn.mrcode.sharding.repo.bean.SOrder;
import cn.mrcode.sharding.repo.bean.SOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SOrderMapper {
    int countByExample(SOrderExample example);

    int deleteByExample(SOrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SOrder record);

    int insertSelective(SOrder record);

    List<SOrder> selectByExample(SOrderExample example);

    SOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SOrder record, @Param("example") SOrderExample example);

    int updateByExample(@Param("record") SOrder record, @Param("example") SOrderExample example);

    int updateByPrimaryKeySelective(SOrder record);

    int updateByPrimaryKey(SOrder record);
}