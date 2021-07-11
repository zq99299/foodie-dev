package cn.mrcode.foodiedev.service;

import cn.mrcode.foodiedev.pojo.OrderStatus;
import cn.mrcode.foodiedev.pojo.bo.ShopcartBO;
import cn.mrcode.foodiedev.pojo.bo.SubmitOrderBO;
import cn.mrcode.foodiedev.pojo.vo.OrderVO;

import java.util.List;

/**
 * @author mrcode
 * @date 2021/2/16 20:10
 */
public interface OrderService {
    /**
     * 用于创建订单相关信息
     *
     * @param shopcartBOList
     * @param submitOrderBO
     */
    OrderVO createOrder(List<ShopcartBO> shopcartBOList, SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param orderStatus
     */
    void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     *
     * @param orderId
     * @return
     */
    OrderStatus queryOrderStatusInfo(String orderId);

    void closeOrder();
}
