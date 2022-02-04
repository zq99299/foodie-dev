package cn.mrcode.sharding.repo.mapper;

import cn.mrcode.sharding.repo.bean.MOrder;
import cn.mrcode.sharding.repo.bean.MOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MOrderMapper {
    int countByExample(MOrderExample example);

    int deleteByExample(MOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MOrder record);

    int insertSelective(MOrder record);

    List<MOrder> selectByExample(MOrderExample example);

    MOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MOrder record, @Param("example") MOrderExample example);

    int updateByExample(@Param("record") MOrder record, @Param("example") MOrderExample example);

    int updateByPrimaryKeySelective(MOrder record);

    int updateByPrimaryKey(MOrder record);
}