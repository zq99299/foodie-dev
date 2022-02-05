package cn.mrcode.xa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author mrcode
 * @date 2022/2/5 19:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class XAServiceTest {
    @Autowired
    private XAService xaService;

    @Test
    public void testXA() {
        xaService.testXA();
    }
}