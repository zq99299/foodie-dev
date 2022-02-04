package cn.mrcode.sharding;

import cn.mrcode.sharding.repo.bean.MOrder;
import cn.mrcode.sharding.repo.mapper.MOrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author mrcode
 * @date 2022/1/29 15:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MOrderTests {
    @Resource
    private MOrderMapper mOrderMapper;
    @Test
    public void testOrderAdd() {
        MOrder mOrder = new MOrder();
        mOrder.setId(1);
        mOrder.setUserId(1);
        mOrder.setOrderAmount(BigDecimal.TEN);
        mOrder.setOrderStatus(2);
        mOrderMapper.insertSelective(mOrder);
    }

    @Test
    public void testOrderAdd2() {
        MOrder mOrder = new MOrder();
        mOrder.setId(2);  // 分到 m_order_1 中
        mOrder.setUserId(2);
        mOrder.setOrderAmount(BigDecimal.TEN);
        mOrder.setOrderStatus(2);
        mOrderMapper.insertSelective(mOrder);
    }

    @Test
    public void testOrderRead() {
        MOrder mOrder = mOrderMapper.selectByPrimaryKey(2);
        System.out.println(mOrder);
    }
}
