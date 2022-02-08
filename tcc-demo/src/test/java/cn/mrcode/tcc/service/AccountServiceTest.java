package cn.mrcode.tcc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author mrcode
 * @date 2022/2/8 21:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Test
    public void transferAccount() {
        accountService.transferAccount();
    }

    @Test
    public void transferAccount2() {
        accountService.transferAccount2();
    }

    @Test
    public void transferAccount3() {
        accountService.transferAccount3();
    }
}