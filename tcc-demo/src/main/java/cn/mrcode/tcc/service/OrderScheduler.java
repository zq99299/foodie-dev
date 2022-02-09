package cn.mrcode.tcc.service;

import cn.mrcode.tcc.dao.db01.bean.PaymentMsg;
import cn.mrcode.tcc.dao.db01.bean.PaymentMsgExample;
import cn.mrcode.tcc.dao.db01.mapper.PaymentMsgMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 订单消息表
 *
 * @author mrcode
 * @date 2022/2/9 22:11
 */
@Service
public class OrderScheduler {
    @Resource
    private PaymentMsgMapper paymentMsgMapper;
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * 10 秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void orderNotify() {
        // 获取 所有未发送的消息
        PaymentMsgExample paymentMsgExample = new PaymentMsgExample();
        paymentMsgExample.createCriteria().andStatusEqualTo(0); // 未发送
        List<PaymentMsg> paymentMsgs = paymentMsgMapper.selectByExample(paymentMsgExample);
        if (paymentMsgs == null || paymentMsgs.size() == 0) return;

        for (PaymentMsg paymentMsg : paymentMsgs) {
            Integer orderId = paymentMsg.getOrderId();
            String result = restTemplate.getForObject("http://localhost:8080/handleOrder?orderId=" + orderId, String.class);
            // 如果成功，则改变消息表状态
            if ("success".equals(result)) {
                paymentMsg.setStatus(1);  // 已发送成功
            }
            // 如果失败，则修改失败的次数，和相关逻辑
            else {
                Integer falureCnt = paymentMsg.getFalureCnt();
                falureCnt++;
                if (falureCnt > 5) {
                    paymentMsg.setStatus(2); // 超过重试次数
                } else {
                    paymentMsg.setFalureCnt(falureCnt);
                }
            }

            paymentMsg.setUpdateUser(0);
            paymentMsg.setUpdateTime(new Date());
            paymentMsgMapper.updateByPrimaryKeySelective(paymentMsg);
        }
    }
}
