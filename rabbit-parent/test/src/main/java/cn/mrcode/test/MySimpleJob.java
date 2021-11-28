package cn.mrcode.test;

import cn.mrcode.rabbit.task.annotation.ElasticJobConfig;
import cn.mrcode.rabbit.task.annotation.EnableElasticJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 简单任务：写业务逻辑
 *
 * @author mrcode
 * @date 2021/11/17 21:01
 */
@ElasticJobConfig(
        cron = "/5 * * * * ?",
        jobName = "cn.mrcode.test.MySimpleJob",
        shardingTotalCount = 2,
        overwrite = true,
        shardingItemParameters = " 0=a,1=b",
        jobParameter = "a"
)
@Component
public class MySimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        // shardingItemParameters = " 0=a,1=b"
        // 在分片执行的时候，会在  shardingContext.shardingParameter 中标识出来，当前这个任务分片 需要执行的参数是什么，比如 a
        // 也就是说：你有 2 台机器，写 shardingTotalCount = 2，然后一台机器会收到 shardingParameter=a,一台会收到 shardingParameter=b
        System.out.println("---- 开始 MySimpleJob -------，shardingContext" + shardingContext);
//        System.out.println("---- 结束 MySimpleJob -------");
    }
}
