package cn.mrcode.distributed.service;

import cn.mrcode.distributed.repo.bean.Orders;
import cn.mrcode.distributed.repo.bean.Product;
import cn.mrcode.distributed.repo.mapper.OrdersMapper;
import cn.mrcode.distributed.repo.mapper.ProductExtMapper;
import cn.mrcode.distributed.repo.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mrcode
 * @date 2022/1/9 17:17
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class OrderService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private ProductExtMapper productExtMapper;

    // 事物管理器
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    // 事物定义
    @Autowired
    private TransactionDefinition transactionDefinition;

    /**
     * @param productId          商品 ID
     * @param purchaseProductNum 购买的商品数量
     */
    // 不使用 spring 提供的事物
    // @Transactional(rollbackFor = Exception.class)
    public Integer createOrder(int productId, int purchaseProductNum) throws Exception {
        TransactionStatus transaction = null;
        try {
            // 讲获取库存、校验库存、扣减库存 放在锁里面
            synchronized (this) {
                // 获取事物
                transaction = platformTransactionManager.getTransaction(transactionDefinition);
                Product product = productMapper.selectByPrimaryKey(productId);
                if (product == null) {
                    throw new Exception("购买商品： " + productId + "不存在");
                }

                // 商品当前库存
                Integer currentCount = product.getCount();
                // 校验库存
                if (purchaseProductNum > currentCount) {
                    throw new Exception("购买商品： " + productId + "仅剩" + currentCount + " 件，无法购买");
                }
                productExtMapper.updateProductCount(productId, purchaseProductNum);
                // 事物提交
                platformTransactionManager.commit(transaction);
            }
        } catch (Exception e) {
            // 事物回滚
            platformTransactionManager.rollback(transaction);
            throw e;
        }


        // 获取事物
        transaction = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            // 创建订单
            Orders record = new Orders();
            record.setProductId(productId);
            ordersMapper.insertSelective(record);
            return record.getId();
        } catch (Exception e) {
            // 事物回滚
            platformTransactionManager.rollback(transaction);
            throw e;
        }
    }
}
