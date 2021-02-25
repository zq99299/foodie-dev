package cn.mrcode.foodiedev.service.center;


import cn.mrcode.foodiedev.common.util.PagedGridResult;
import cn.mrcode.foodiedev.pojo.OrderItems;
import cn.mrcode.foodiedev.pojo.bo.center.OrderItemsCommentBO;

import java.util.List;

public interface MyCommentsService {

    /**
     * 根据订单id查询关联的商品
     *
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户的评论
     *
     * @param orderId
     * @param userId
     * @param commentList
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);


    /**
     * 我的评价查询 分页
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
