package cn.mrcode.esjob.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author mrcode
 * @date 2021/11/22 21:29
 */
public class MyDataflowJob implements DataflowJob<String> {
    private int count = 1;

    /**
     * 获取待处理的数据
     * <pre>
     *     处理逻辑与 streamingProcess 参与有关：
     *     false: 每次任务只会执行一次 抓取数据 和 处理数据，如果返回数据为 空，则不会执行抓取数据
     *     true: 只要返回结果有数据，就会一直执行，如果返回 null 或则 空集合，就会停止该次任务
     * </pre>
     *
     * @param shardingContext
     * @return
     */
    @Override
    public List<String> fetchData(ShardingContext shardingContext) {
        System.err.println("------  @@@@ 抓取数据集合 ---------" + this + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        if (count > 0) {
            count--;
            return Arrays.asList(count + "");
        }
        return null;
    }

    /**
     * 处理数据
     *
     * @param shardingContext
     * @param data
     */
    @Override
    public void processData(ShardingContext shardingContext, List<String> data) {
        System.err.println("------  @@@@ 处理数据集合 ---------");
    }
}
