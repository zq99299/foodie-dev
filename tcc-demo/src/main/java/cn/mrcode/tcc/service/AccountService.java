package cn.mrcode.tcc.service;

import cn.mrcode.tcc.dao.db01.bean.Account1;
import cn.mrcode.tcc.dao.db01.mapper.Account1Mapper;
import cn.mrcode.tcc.dao.db02.bean.Account2;
import cn.mrcode.tcc.dao.db02.mapper.Account2Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author mrcode
 * @date 2022/2/8 20:57
 */
@Service
public class AccountService {
    @Resource
    private Account1Mapper account1Mapper;

    @Resource
    private Account2Mapper account2Mapper;

    /**
     * 无补偿的出问题场景
     */
    @Transactional
    public void transferAccount() {
        Account1 accountA = account1Mapper.selectByPrimaryKey(1);
        // 从 A 的账户扣减 200
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal("200")));
        account1Mapper.updateByPrimaryKeySelective(accountA);

        int i = 1 / 0;  // 手动造成一个异常

        // 从 B 的账户增加 200
        Account2 accountB = account2Mapper.selectByPrimaryKey(2);
        accountB.setBalance(accountB.getBalance().add(new BigDecimal("200")));
        account2Mapper.updateByPrimaryKeySelective(accountB);
    }

    /**
     * 无补偿,指定数据源 A 的事物管理器
     */
    @Transactional(transactionManager = "tm01")
    public void transferAccount2() {
        Account1 accountA = account1Mapper.selectByPrimaryKey(1);
        // 从 A 的账户扣减 200
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal("200")));
        account1Mapper.updateByPrimaryKeySelective(accountA);


        // 从 B 的账户增加 200
        Account2 accountB = account2Mapper.selectByPrimaryKey(2);
        accountB.setBalance(accountB.getBalance().add(new BigDecimal("200")));
        account2Mapper.updateByPrimaryKeySelective(accountB);

        int i = 1 / 0;  // 手动造成一个异常
    }


    /**
     * 事务补偿写法,指定数据源 A 的事物管理器
     */
    @Transactional(transactionManager = "tm01")
    public void transferAccount3() {
        Account1 accountA = account1Mapper.selectByPrimaryKey(1);
        // 从 A 的账户扣减 200
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal("200")));
        account1Mapper.updateByPrimaryKeySelective(accountA);

        // 从 B 的账户增加 200
        Account2 accountB = account2Mapper.selectByPrimaryKey(2);
        accountB.setBalance(accountB.getBalance().add(new BigDecimal("200")));
        account2Mapper.updateByPrimaryKeySelective(accountB);

        try {
            int i = 1 / 0;  // 手动造成一个异常
        } catch (Exception e) {
            // 补偿事务，前面增加了 200，这里减掉 200
            Account2 accountB1 = account2Mapper.selectByPrimaryKey(2);
            accountB1.setBalance(accountB1.getBalance().subtract(new BigDecimal("200")));
            account2Mapper.updateByPrimaryKeySelective(accountB1);

            // 然后再把异常抛出去，让 Account1 的本地事物回滚
            throw e;
        }
    }
}
