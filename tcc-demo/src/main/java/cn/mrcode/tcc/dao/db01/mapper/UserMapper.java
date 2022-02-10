package cn.mrcode.tcc.dao.db01.mapper;

import cn.mrcode.tcc.dao.db01.bean.User;
import cn.mrcode.tcc.dao.db01.bean.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    @Select("select * from t_user")
    @ResultMap("BaseResultMap") // 指定 UserMapper.xml 中定义的 resultMap
    List<User> selectAllUsers();
}