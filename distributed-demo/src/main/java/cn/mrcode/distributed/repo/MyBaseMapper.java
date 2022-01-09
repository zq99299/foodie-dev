package cn.mrcode.distributed.repo;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author mrcode
 * @date 2022/1/9 17:47
 */
@Repository
public interface MyBaseMapper<T>  extends Mapper<T>, MySqlMapper<T>, ExampleMapper<T> {
}
