package cn.mrcode.foodiedev.service.impl;

import cn.mrcode.foodiedev.pojo.Stu;
import cn.mrcode.foodiedev.service.StuService;
import cn.mrcode.foodiedev.service.TestTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试事物
 */
@Service
public class TestTransServiceImpl implements TestTransService {
    @Autowired
    private StuService stuService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void testPropagationTrans() {
        stuService.saveParent();
        try {

            stuService.saveChildren();
        } catch (Exception e) {

        }

//        // 模拟一个异常
        int a = 1 / 0;
    }
}
