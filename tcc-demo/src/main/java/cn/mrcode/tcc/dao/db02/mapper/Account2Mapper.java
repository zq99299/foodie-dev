package cn.mrcode.tcc.dao.db02.mapper;

import cn.mrcode.tcc.dao.db02.bean.Account2;
import cn.mrcode.tcc.dao.db02.bean.Account2Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Account2Mapper {
    int countByExample(Account2Example example);

    int deleteByExample(Account2Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(Account2 record);

    int insertSelective(Account2 record);

    List<Account2> selectByExample(Account2Example example);

    Account2 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Account2 record, @Param("example") Account2Example example);

    int updateByExample(@Param("record") Account2 record, @Param("example") Account2Example example);

    int updateByPrimaryKeySelective(Account2 record);

    int updateByPrimaryKey(Account2 record);
}