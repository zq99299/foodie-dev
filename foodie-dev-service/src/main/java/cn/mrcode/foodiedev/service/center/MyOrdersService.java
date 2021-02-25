package cn.mrcode.foodiedev.service.center;


import cn.mrcode.foodiedev.common.util.PagedGridResult;
import cn.mrcode.foodiedev.pojo.Orders;
import cn.mrcode.foodiedev.pojo.vo.OrderStatusCountsVO;

public interface MyOrdersService {

    /**
     * 查询我的订单列表
     *
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyOrders(String userId,
                                  Integer orderStatus,
                                  Integer page,
                                  Integer pageSize);

    /**
     * @Description: 订单状态 --> 商家发货
     */
    void updateDeliverOrderStatus(String orderId);

    /**
     * 查询我的订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    Orders queryMyOrder(String userId, String orderId);

    /**
     * 更新订单状态 —> 确认收货
     *
     * @return
     */
    boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单（逻辑删除）
     * @param userId
     * @param orderId
     * @return
     */
    boolean deleteOrder(String userId, String orderId);

    /**
     * 查询用户订单数
     * @param userId
     */
    OrderStatusCountsVO getOrderStatusCounts(String userId);

    /**
     * 获得分页的订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult getOrdersTrend(String userId,
                                   Integer page,
                                   Integer pageSize);
}