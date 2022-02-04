package cn.mrcode.sharding.repo.bean;

import lombok.Data;
import lombok.ToString;

/**
 * @author mrcode
 * @date 2022/1/29 13:14
 */
@Data
@ToString
public class OrderDetail {
    private Integer orderId;
    private Integer userId;
    private Integer orderItemId;
    private String productName;
}
