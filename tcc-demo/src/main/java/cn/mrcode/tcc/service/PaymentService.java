package cn.mrcode.tcc.service;

import cn.mrcode.tcc.dao.db01.bean.Account1;
import cn.mrcode.tcc.dao.db01.bean.PaymentMsg;
import cn.mrcode.tcc.dao.db01.mapper.Account1Mapper;
import cn.mrcode.tcc.dao.db01.mapper.PaymentMsgMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付接口，主要操作 db01 的数据库
 *
 * @author mrcode
 * @date 2022/2/9 21:42
 */
@Service
public class PaymentService {
    @Resource
    private Account1Mapper account1Mapper;
    @Resource
    private PaymentMsgMapper paymentMsgMapper;

    /**
     * 支付接口
     *
     * @param userId  用户 id
     * @param orderId 订单 id
     * @param amount  支付的金额
     * @return 0:支付成功 1: 用户不存在, 2: 余额不足
     */
    // 加事物，下面的两个操作都是同一个数据库中的
    @Transactional(transactionManager = "tm01")
    public int payment(int userId, int orderId, BigDecimal amount) {
        Account1 account = account1Mapper.selectByPrimaryKey(userId);
        if (account == null) {
            return 1;
        }
        if (account.getBalance().compareTo(amount) < 0) {
            return 2;
        }

        // 扣减余额，支付操作
        account.setBalance(account.getBalance().subtract(amount));
        account1Mapper.updateByPrimaryKeySelective(account);

        // 写消息表
        PaymentMsg paymentMsg = new PaymentMsg();
        paymentMsg.setOrderId(orderId);
        paymentMsg.setStatus(0);  // 未发送
        paymentMsg.setFalureCnt(0);  // 失败次数
        paymentMsg.setCreateTime(new Date());
        paymentMsg.setCreateUser(userId);
        paymentMsg.setUpdateTime(new Date());
        paymentMsg.setUpdateUser(userId);

        paymentMsgMapper.insertSelective(paymentMsg);

        return 0;
    }
}
