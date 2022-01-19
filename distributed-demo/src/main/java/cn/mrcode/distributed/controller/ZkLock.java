package cn.mrcode.distributed.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @author mrcode
 * @date 2022/1/19 21:18
 */
@Slf4j
public class ZkLock implements AutoCloseable, Watcher {
    private ZooKeeper zooKeeper;

    /**
     * 当线程创建的路径
     */
    private String znode;

    /**
     * @param connectString  zk 服务器，多个用英文逗号分隔，比如 127.0.0.1:2181
     * @param sessionTimeout 会话超时时间，单位毫秒
     */
    public ZkLock(String connectString, int sessionTimeout) throws IOException {
        this.zooKeeper = new ZooKeeper(connectString, sessionTimeout, this);
    }

    /**
     * tye-resource 自动关闭语法
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        zooKeeper.delete(znode, -1);
        zooKeeper.close();
        log.info("释放锁");
    }

    /**
     * 观察器
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        // 当 前一个节点被删除时，表示可以唤醒获取锁的等待，也意味着当前线程获得了锁
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            synchronized (this) {
                notify();
            }
        }
    }

    /**
     * 获取锁
     *
     * @param businessCode 按业务类型设置 锁
     * @return
     */
    public boolean getLock(String businessCode) {
        try {
            // 判断节点是否存在，如果不存在则返回 null
            String path = "/" + businessCode;
            Stat stat = zooKeeper.exists(path, false);
            if (stat == null) {
                // 首先一个业务一个持久节点，然后在持久节点里面创建瞬时节点
                zooKeeper.create(path,
                        businessCode.getBytes(StandardCharsets.UTF_8),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,  // 公开权限，不使用用户名密码就能访问这个节点
                        CreateMode.PERSISTENT);
            }

            // 创建瞬时有序节点，多线程直接创建用于排序
            // /order/order_1
            String subPath = "/" + businessCode + "/" + businessCode + "_";
            znode = zooKeeper.create(subPath,
                    businessCode.getBytes(StandardCharsets.UTF_8),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL
            );

            // 获取该业务节点下的所有瞬时节点，然后排序比较
            /*
                注意：这里返回的是业务下面的 子节点
                比如： /order/order_1，返回的是 /order 下面的 order_1
             */
            List<String> childrenNodes = zooKeeper.getChildren(path, false);
            Collections.sort(childrenNodes);  // 升序排列
            // 如果列表中的第一个是当前创建的节点，则表示获得了锁
            String firstNode = childrenNodes.get(0);

            // znode 是全路径，而 firstNode 是某一级节点的路径，所以需要用 endsWith 来判定
            // 相等，则获得锁
            if (znode.endsWith(firstNode)) {
                return true;
            }

            // 如果没有获得锁，则监听前一个节点
            String lastNode = firstNode;
            for (String node : childrenNodes) {
                // 如果找到当前节点，则监听前一个节点
                // 这里一定会有前一个节点，因为如果是首个节点的话，就获得锁了
                if (znode.endsWith(node)) {
                    zooKeeper.exists("/" + businessCode + "/" + lastNode, this);
                    break;
                } else {
                    lastNode = node;
                }
            }

            // 加锁，当前线程等待
            synchronized (this) {
                // 注意：wait 方法会让当前线程阻塞，并且会释放锁
                // 由于这个特性，上面的监视器中同步代码块才会获得锁并执行 notify
                wait();
            }
            return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
