package cn.mrcode.xa.dao.db02.mapper;

import cn.mrcode.xa.dao.db02.bean.XA02;
import cn.mrcode.xa.dao.db02.bean.XA02Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface XA02Mapper {
    int countByExample(XA02Example example);

    int deleteByExample(XA02Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(XA02 record);

    int insertSelective(XA02 record);

    List<XA02> selectByExample(XA02Example example);

    XA02 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XA02 record, @Param("example") XA02Example example);

    int updateByExample(@Param("record") XA02 record, @Param("example") XA02Example example);

    int updateByPrimaryKeySelective(XA02 record);

    int updateByPrimaryKey(XA02 record);
}