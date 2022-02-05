package cn.mrcode.xa.dao.db01.mapper;

import cn.mrcode.xa.dao.db01.bean.XA01;
import cn.mrcode.xa.dao.db01.bean.XA01Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface XA01Mapper {
    int countByExample(XA01Example example);

    int deleteByExample(XA01Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(XA01 record);

    int insertSelective(XA01 record);

    List<XA01> selectByExample(XA01Example example);

    XA01 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XA01 record, @Param("example") XA01Example example);

    int updateByExample(@Param("record") XA01 record, @Param("example") XA01Example example);

    int updateByPrimaryKeySelective(XA01 record);

    int updateByPrimaryKey(XA01 record);
}