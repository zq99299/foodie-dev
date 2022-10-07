package cn.mrcode.foodiedev.api;

import cn.mrcode.foodiedev.Application;
import cn.mrcode.foodiedev.service.StuService;
import cn.mrcode.foodiedev.service.impl.TestTransServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class TransTest {
    @Autowired
    private TestTransServiceImpl testTransService;

    //    @Test
    public void myTest() {
        testTransService.testPropagationTrans();
    }

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        while (true) {
            TimeUnit.SECONDS.sleep(5);
            ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:8080/index/sixNewItems/1", String.class);
            System.out.println(forEntity);
        }
    }
}
