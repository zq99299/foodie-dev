package cn.mrcode.sharding;

import cn.mrcode.sharding.repo.bean.*;
import cn.mrcode.sharding.repo.mapper.AreaMapper;
import cn.mrcode.sharding.repo.mapper.OrderItemMapper;
import cn.mrcode.sharding.repo.mapper.OrderItemxMapper;
import cn.mrcode.sharding.repo.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

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

    @Test
    public void testOrder2() {
        Order order = new Order();
        // 数据库：userId 偶数分到 sharding-order，奇数分到 shard-order
        order.setUserId(20);
        // 表：id 偶数分到 t_order_1, 奇数分到 t_order_2
        order.setId(2);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        orderMapper.insertSelective(order);

        // 那么这条语句期望是插入到：sharding-order.t_order_1 中
    }

    @Test
    public void testSelectOrder() {
        OrderExample example = new OrderExample();
        example.createCriteria()
                .andIdEqualTo(2)
                .andUserIdEqualTo(20);
        List<Order> orders = orderMapper.selectByExample(example);
        for (Order order : orders) {
            System.out.println("id" + order.getId() + " , userId=" + order.getUserId());
        }
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private AreaMapper areaMapper;

    /**
     * 测试全局表
     */
    @Test
    public void testGlobal() {
        Area area = new Area();
        area.setId(2);
        area.setName("上海");
        areaMapper.insert(area);
        // 正确结果是：两个数据库中的 area 表中都会插入数据
    }

    @Test
    public void testGlobalSearch() {
        AreaExample areaExample = new AreaExample();
        areaExample.createCriteria()
                .andIdEqualTo(2);
        List<Area> areas = areaMapper.selectByExample(areaExample);
        System.out.println(areas.size());
    }


    @Autowired
    private OrderItemMapper orderItemMapper;

    /**
     * 绑定表的子表插入数据
     */
    @Test
    public void testBindingTable() {
        OrderItem order = new OrderItem();
        // 数据库：userId 偶数分到 sharding-order，奇数分到 shard-order
        order.setUserId(19);
        // 表：orderId 偶数分到 t_order_item_1, 奇数分到 t_order_item_2
        order.setId(1);
        order.setOrderId(1);
        order.setProductName("商品 1");
        orderItemMapper.insertSelective(order);

        // 那么这条语句期望是插入到：shard-order.t_order_item_2 中
    }

    @Autowired
    private OrderItemxMapper orderItemxMapper;

    @Test
    public void testBingdingTables() {
        List<OrderDetail> details = orderItemxMapper.selectOrder();
        System.out.println(details);
    }
}
