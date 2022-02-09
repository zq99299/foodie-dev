package cn.mrcode.tcc.controller;

import cn.mrcode.tcc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mrcode
 * @date 2022/2/9 21:59
 */
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("handleOrder")
    public String handleOrder(int orderId) {
        try {
            int result = orderService.handleOrder(orderId);
            return result == 0 ? "success" : "fail";
        } catch (Exception e) {
            return "fail";
        }
    }
}
