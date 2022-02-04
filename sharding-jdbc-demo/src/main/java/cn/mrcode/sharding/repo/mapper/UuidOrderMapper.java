package cn.mrcode.sharding.repo.mapper;

import cn.mrcode.sharding.repo.bean.UuidOrder;
import cn.mrcode.sharding.repo.bean.UuidOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UuidOrderMapper {
    int countByExample(UuidOrderExample example);

    int deleteByExample(UuidOrderExample example);

    int deleteByPrimaryKey(String id);

    int insert(UuidOrder record);

    int insertSelective(UuidOrder record);

    List<UuidOrder> selectByExample(UuidOrderExample example);

    UuidOrder selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") UuidOrder record, @Param("example") UuidOrderExample example);

    int updateByExample(@Param("record") UuidOrder record, @Param("example") UuidOrderExample example);

    int updateByPrimaryKeySelective(UuidOrder record);

    int updateByPrimaryKey(UuidOrder record);
}