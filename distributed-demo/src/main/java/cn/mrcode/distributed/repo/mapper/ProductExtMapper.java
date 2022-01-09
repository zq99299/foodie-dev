package cn.mrcode.distributed.repo.mapper;

import cn.mrcode.distributed.repo.MyBaseMapper;
import cn.mrcode.distributed.repo.bean.Product;
import org.apache.ibatis.annotations.Param;

public interface ProductExtMapper {

    int updateProductCount(@Param("productId") int productId,
                            @Param("purchaseProductNum") int purchaseProductNum);
}