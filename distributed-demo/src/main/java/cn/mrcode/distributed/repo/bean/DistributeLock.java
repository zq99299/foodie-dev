package cn.mrcode.distributed.repo.bean;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "`distribute_lock`")
public class DistributeLock {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 一个业务类型一个code
     */
    @Column(name = "`business_code`")
    private String businessCode;

    /**
     * 描述
     */
    @Column(name = "`business_name`")
    private String businessName;
}