package cn.mrcode.distributed.repo.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "orders")
public class Orders {
    @GeneratedValue(generator = "JDBC")
    @Id
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;
}