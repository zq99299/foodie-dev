package cn.mrcode.sharding.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * 精确分片算法自定义实现
 * @author mrcode
 * @date 2022/2/4 11:47
 */
public class MySharding implements PreciseShardingAlgorithm<String>{
    /**
     * 分片键路由计算
     * @param availableTargetNames 可用的数据源或表的名称
     * @param shardingValue 分片值
     * @return 返回数据源或表名的分片结果
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        System.out.println(shardingValue);
        System.out.println(availableTargetNames);

        String value = shardingValue.getValue();
        // 这里使用 UUID 的 hashCode 来取模
        // hashCode 有可能是负数，所以取绝对值
        int mode = Math.abs(value.hashCode()) % availableTargetNames.size();
        int index  =  mode + 1;
        String[] strings = availableTargetNames.toArray(new String[availableTargetNames.size()]);
        return strings[index];
    }
}
