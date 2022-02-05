package cn.mrcode.xa.service;

import cn.mrcode.xa.dao.db01.bean.XA01;
import cn.mrcode.xa.dao.db01.mapper.XA01Mapper;
import cn.mrcode.xa.dao.db02.bean.XA02;
import cn.mrcode.xa.dao.db02.mapper.XA02Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author mrcode
 * @date 2022/2/5 19:53
 */
@Service
public class XAService {
    @Resource
    private XA01Mapper xa01Mapper;
    @Resource
    private XA02Mapper xa02Mapper;

    // 加事物注解，并指定事物管理器
    @Transactional(transactionManager = "xaTransaction")
    public void testXA() {
        XA01 xa01 = new XA01();
        xa01.setId(1);
        xa01.setName("xa01");
        xa01Mapper.insertSelective(xa01);

        XA02 xa02 = new XA02();
        xa02.setId(1);
        xa02.setName("xa02");
        xa02Mapper.insertSelective(xa02);
    }
}
