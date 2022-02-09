package cn.mrcode.tcc.controller;

import cn.mrcode.tcc.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author mrcode
 * @date 2022/2/9 21:51
 */
@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @RequestMapping("payment")
    public String payment(int userId, int orderId, BigDecimal amount) {
        int result = paymentService.payment(userId, orderId, amount);
        return "支付结果：" + result;
    }
}
