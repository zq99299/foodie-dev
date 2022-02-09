package cn.mrcode.tcc.service;

import cn.mrcode.tcc.dao.db02.bean.Order;
import cn.mrcode.tcc.dao.db02.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author mrcode
 * @date 2022/2/9 21:56
 */
@Service
public class OrderService {
    @Resource
    private OrderMapper orderMapper;

    /**
     * 订单回调接口
     *
     * @param orderId
     * @return 0:操作成功， 1: 订单不存在
     */
    public int handleOrder(int orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);

        int a = 1 / 0; // 模拟通知接口处理异常

        if (order == null) {
            return 1;
        }

        order.setOrderStatus(1); // 已支付
        order.setUpdateTime(new Date());
        order.setUpdateUser(0); // 系统更新
        orderMapper.updateByPrimaryKeySelective(order);
        return 0;
    }
}
