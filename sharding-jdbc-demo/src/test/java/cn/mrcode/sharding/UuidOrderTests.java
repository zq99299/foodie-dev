package cn.mrcode.sharding;

import cn.mrcode.sharding.repo.bean.Order;
import cn.mrcode.sharding.repo.bean.UuidOrder;
import cn.mrcode.sharding.repo.mapper.UuidOrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author mrcode
 * @date 2022/2/4 11:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UuidOrderTests {
    @Resource
    private UuidOrderMapper uuidOrderMapper;
    @Test
    public void testOrder() {
        UuidOrder order = new UuidOrder();
        // 数据库：userId 偶数分到 sharding-order，奇数分到 shard-order
        order.setUserId(19);
        // 表：id 由于使用 UUID，可以自己在这里产生 UUID 再 set
        // 但是一般不会自己在程序中产生 ID，使用 sharding 的功能产生 UUID
        // order.setId(1);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        uuidOrderMapper.insertSelective(order);

        // 那么这条语句期望是插入到：shard-order.uuid_order_2 中
    }
}
