package cn.mrcode.esjob.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * 简单任务：写业务逻辑
 *
 * @author mrcode
 * @date 2021/11/17 21:01
 */
public class MySimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("---- 开始 MySimpleJob -------，当前线程："+ Thread.currentThread().getName());
//        System.out.println("---- 结束 MySimpleJob -------");
    }
}
