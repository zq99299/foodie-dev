package cn.mrcode.distributed.repo.mapper;

import cn.mrcode.distributed.repo.bean.DistributeLock;
import org.apache.ibatis.annotations.Param;

/**
 * @author mrcode
 * @date 2022/1/10 21:22
 */
public interface DistributeLockExtMapper {
    DistributeLock select(@Param("businessCode") String businessCode);
}
