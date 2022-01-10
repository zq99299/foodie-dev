package cn.mrcode.distributed.repo.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "`product`")
public class Product {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商品库存数量
     */
    @Column(name = "`count`")
    private Integer count;
}