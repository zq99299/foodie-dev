package cn.mrcode.xa.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * 配置事物管理器
 *
 * @author mrcode
 * @date 2022/2/5 18:06
 */
@Configuration
public class ConfigXa {
    @Bean("xaTransaction")
    public JtaTransactionManager jtaTransactionManager() {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        return new JtaTransactionManager(userTransactionImp, userTransactionManager);
    }
}
