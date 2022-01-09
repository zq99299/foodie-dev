package cn.mrcode.distributed.repo.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "product")
public class Product {
    @GeneratedValue(generator = "JDBC")
    @Id
    private Integer id;

    /**
     * 商品库存数量
     */
    private Integer count;
}