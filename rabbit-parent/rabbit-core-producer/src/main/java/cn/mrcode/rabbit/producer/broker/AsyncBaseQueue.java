package cn.mrcode.rabbit.producer.broker;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author mrcode
 * @date 2021/10/20 22:25
 */
@Slf4j
public class AsyncBaseQueue {
    /**
     * 线程数量，获取 CPU 核数
     */
    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();
    /**
     * 队列大小
     */
    private static final int QUEUE_SIZE = 10000;

    /**
     * 自定义线程池
     */
    private final static ExecutorService senderAsync = new ThreadPoolExecutor(
            // 线程池类最少保持的线程数量
            THREAD_SIZE,
            // 线程池类最多容纳的线程数量
            THREAD_SIZE,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(QUEUE_SIZE),
            // 线程池工厂
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    // 设置线程的名称
                    t.setName("rabbitmq_client_async_sender");
                    return t;
                }
            },
            // 拒绝策略
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    log.error("async sender is err rejected, runnable: {}, executor: {}", r, executor);
                }
            });

    /**
     * 提交任务到线程池中
     *
     * @param runnable
     */
    public static void submit(Runnable runnable) {
        senderAsync.submit(runnable);
    }
}
