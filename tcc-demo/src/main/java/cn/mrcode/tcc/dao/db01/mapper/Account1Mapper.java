package cn.mrcode.tcc.dao.db01.mapper;

import cn.mrcode.tcc.dao.db01.bean.Account1;
import cn.mrcode.tcc.dao.db01.bean.Account1Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Account1Mapper {
    int countByExample(Account1Example example);

    int deleteByExample(Account1Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(Account1 record);

    int insertSelective(Account1 record);

    List<Account1> selectByExample(Account1Example example);

    Account1 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Account1 record, @Param("example") Account1Example example);

    int updateByExample(@Param("record") Account1 record, @Param("example") Account1Example example);

    int updateByPrimaryKeySelective(Account1 record);

    int updateByPrimaryKey(Account1 record);
}