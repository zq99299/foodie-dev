package cn.mrcode.foodiedev.api;

import cn.mrcode.foodiedev.Application;
import cn.mrcode.foodiedev.service.StuService;
import cn.mrcode.foodiedev.service.impl.TestTransServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransTest {
    @Autowired
    private TestTransServiceImpl testTransService;

    @Test
    public void myTest() {
        testTransService.testPropagationTrans();
    }

}
