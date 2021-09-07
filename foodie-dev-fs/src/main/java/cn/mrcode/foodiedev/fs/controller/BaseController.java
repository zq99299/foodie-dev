package cn.mrcode.foodiedev.fs.controller;

import cn.mrcode.foodiedev.common.util.RedisOperator;
import cn.mrcode.foodiedev.pojo.Users;
import cn.mrcode.foodiedev.pojo.vo.UsersVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class BaseController {
    @Autowired
    private RedisOperator redisOperator;

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIE_SHOPCART = "shopcart";
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";		// produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url
    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    public UsersVO convertVo(Users user) {
        // 实现用户的 redis 会话
        String uniqueToken = UUID.randomUUID().toString();
        // 永远不过期，除非自动退出
        // redis_user_token
        redisOperator.set(BaseController.REDIS_USER_TOKEN + ":" + user.getId(), uniqueToken);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUniqueToken(uniqueToken);
        return usersVO;
    }
}
