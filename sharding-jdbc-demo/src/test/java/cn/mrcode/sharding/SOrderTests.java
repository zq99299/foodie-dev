package cn.mrcode.sharding;

import cn.mrcode.sharding.repo.bean.SOrder;
import cn.mrcode.sharding.repo.bean.UuidOrder;
import cn.mrcode.sharding.repo.mapper.SOrderMapper;
import cn.mrcode.sharding.repo.mapper.UuidOrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author mrcode
 * @date 2022/2/4 11:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SOrderTests {
    @Resource
    private SOrderMapper sOrderMapper;
    @Test
    public void testOrder() {
        SOrder order = new SOrder();
        // 数据库：userId 偶数分到 sharding-order，奇数分到 shard-order
        order.setUserId(19);
        // 表：id ，使用雪花算法生成，偶数分到 s_order_1,基数分到 s_order_2
        // order.setId(1);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        sOrderMapper.insertSelective(order);
    }

    /**
     * 分布式事务测试：本地事务
     */
    @Test
    @Transactional
    public void testOrder2() {
        SOrder order = new SOrder();
        // 数据库：userId 偶数分到 sharding-order，奇数分到 shard-order
        order.setUserId(19);
        // 表：id ，使用雪花算法生成，偶数分到 s_order_1,基数分到 s_order_2
        // order.setId(1);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(-1);
        sOrderMapper.insertSelective(order);

        SOrder order2 = new SOrder();
        // 数据库：userId 偶数分到 sharding-order，奇数分到 shard-order
        order2.setUserId(20);
        // 表：id ，使用雪花算法生成，偶数分到 s_order_1,基数分到 s_order_2
        // order.setId(1);
        order2.setOrderAmount(BigDecimal.TEN);
        order2.setOrderStatus(-1);
        sOrderMapper.insertSelective(order2);
    }
}
