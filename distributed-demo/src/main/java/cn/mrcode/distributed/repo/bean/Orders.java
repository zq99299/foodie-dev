package cn.mrcode.distributed.repo.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "`orders`")
public class Orders {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "`product_id`")
    private Integer productId;
}