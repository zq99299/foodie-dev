package cn.mrcode.sharding.repo.mapper;

import cn.mrcode.sharding.repo.bean.OrderDetail;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

/**
 * @author mrcode
 * @date 2022/1/29 13:12
 */
public interface OrderItemxMapper {
    List<OrderDetail> selectOrder();
}
