package cn.mrcode.sharding;

import cn.mrcode.sharding.repo.bean.Order;
import cn.mrcode.sharding.repo.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @author mrcode
 * @date 2022/1/26 22:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingJdbcDemoTests {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void testOrder() {
        Order order = new Order();
        // 数据库：userId 偶数分到 sharding-order，奇数分到 shard-order
        order.setUserId(19);
        // 表：id 偶数分到 t_order_1, 奇数分到 t_order_2
        order.setId(1);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        orderMapper.insertSelective(order);

        // 那么这条语句期望是插入到：shard-order.t_order_2 中
    }
}
