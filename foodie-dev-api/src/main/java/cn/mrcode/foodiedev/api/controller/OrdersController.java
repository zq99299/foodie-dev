package cn.mrcode.foodiedev.api.controller;

import cn.mrcode.foodiedev.common.enums.OrderStatusEnum;
import cn.mrcode.foodiedev.common.enums.PayMethod;
import cn.mrcode.foodiedev.common.util.CookieUtils;
import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.common.util.JsonUtils;
import cn.mrcode.foodiedev.common.util.RedisOperator;
import cn.mrcode.foodiedev.pojo.OrderStatus;
import cn.mrcode.foodiedev.pojo.bo.ShopcartBO;
import cn.mrcode.foodiedev.pojo.bo.SubmitOrderBO;
import cn.mrcode.foodiedev.pojo.vo.MerchantOrdersVO;
import cn.mrcode.foodiedev.pojo.vo.OrderVO;
import cn.mrcode.foodiedev.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(value = "订单相关", tags = {"订单相关 API 接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {
    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 这里的业务场景规定为：按 session 来保证唯一，这个可以根据业务需求定义
     *
     * @param httpSession
     * @return
     */
    @ApiOperation(value = "用户下单 - toekn 获取", httpMethod = "GET")
    @PostMapping("/getOrderToken")
    public JSONResult getOrderToken(HttpSession httpSession) {
        String token = UUID.randomUUID().toString();
        // 存入 redis：key 根据业务需求进行，value：就是 token 信息
        // 设置 60 秒后过期
        redisOperator.set("ORDER_TOKEN_" + httpSession.getId(), token, 60);
        return JSONResult.ok(token);
    }

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {
        String lockKey = "LOCK_KEY_" + request.getSession().getId();
        // 加分布式锁，进行处理
        RLock lock = redissonClient.getLock(lockKey);
        // 根据订单执行的耗时，设置锁的超时时间
        lock.lock(5, TimeUnit.SECONDS);
        try {
            String orderTokenKey = "ORDER_TOKEN_" + request.getSession().getId();
            // 在 redis 中获取该值，并判定
            String orderToken = redisOperator.get(orderTokenKey);
            if (StringUtils.isBlank(orderToken)) {
                return JSONResult.errorMsg("orderToken 不存在");
            }
            boolean corretToken = orderToken.equals(submitOrderBO.getOrderToken());
            if (!corretToken) {
                return JSONResult.errorMsg("orderToken 不正确");
            }
            // 需要保证该 token 只能被消费一次,校验完成后删除
            redisOperator.del(orderTokenKey);
        } finally {
            try {
                // 释放锁有可能报错
                // 比如你在 debug 的时候，上面锁设置 5 秒就超时，然后超时后，释放锁的话就会异常
                lock.unlock();
            } catch (Exception e) {
            }
        }

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return JSONResult.errorMsg("支付方式不支持！");
        }

//        System.out.println(submitOrderBO.toString());

        String key = BaseController.FOODIE_SHOPCART + ":" + submitOrderBO.getUserId();
        String shopcartJson = redisOperator.get(key);
        if (StringUtils.isBlank(shopcartJson)) {
            return JSONResult.errorMsg("购物车数据不正确");
        }
        List<ShopcartBO> shopcartBOList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);

        // 1. 创建订单: 将购物车也传递进去
        OrderVO orderVO = orderService.createOrder(shopcartBOList, submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        // 这里直接清空购物车，后续讲  redis 之后，再来完善移除已购产品的操作
        // 从现有的购物数据中删除已经购买的
        shopcartBOList.removeAll(orderVO.getToBeRemovedShopcatdList());
        // 更新已经删除了购买商品的购物车
        redisOperator.set(key, JsonUtils.objectToJson(shopcartBOList));
        // 更新 cookie 的购物数据
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartBOList), true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JSONResult> responseEntity = restTemplate.postForEntity(
                paymentUrl,
                entity,
                JSONResult.class);
        JSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            logger.error("发送错误：{}", paymentResult.getMsg());
            return JSONResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }

        return JSONResult.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public JSONResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JSONResult.ok(orderStatus);
    }
}
