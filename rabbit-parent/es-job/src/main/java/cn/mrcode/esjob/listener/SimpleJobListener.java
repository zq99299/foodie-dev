package cn.mrcode.esjob.listener;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mrcode
 * @date 2021/11/16 23:06
 */
public class SimpleJobListener implements ElasticJobListener {
    private static Logger LOGGER = LoggerFactory.getLogger(SimpleJobListener.class);

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        LOGGER.info("-----任务执行之前：{}", JSON.toJSONString(shardingContexts));
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        LOGGER.info("-----任务执行之后：{}", JSON.toJSONString(shardingContexts));
    }
}
