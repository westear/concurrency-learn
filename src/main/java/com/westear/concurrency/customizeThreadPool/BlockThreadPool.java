package com.westear.concurrency.customizeThreadPool;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义阻塞型线程池 当池满时会阻塞任务提交
 */
public class BlockThreadPool {

    private ThreadPoolExecutor pool;

    public BlockThreadPool(int poolSize) {
        pool = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(5), new CustomThreadFactory(),
                new CustomRejectedExecutionHandler());
    }

    public void destroy() {
        if (pool != null) {
            pool.shutdownNow();
        }
    }

    /**
     * 自定义线程工厂
     */
    private static class CustomThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private CustomThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(group,r);
            String threadName = BlockThreadPool.class.getSimpleName()
                    + "-pool-" + poolNumber.getAndIncrement()
                    + "-thread-" + threadNumber.getAndIncrement();
            t.setName(threadName);
            return t;
        }
    }

    /**
     * 自定义拒绝方法
     */
    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                // 核心改造点，由blockingQueue的offer改成put阻塞方法
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(Runnable runnable) {
        this.pool.execute(runnable);
    }

    // 测试构造的线程池
    public static void main(String[] args) {
        BlockThreadPool pool = new BlockThreadPool(3);
        for (int i = 1; i < 100; i++) {
            System.out.println("提交第" + i + "个任务!");
            pool.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getId() + "=====开始");
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println(Thread.currentThread().getId() + "=====【结束】");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("【提交第" + i + "个任务成功！】");
        }

        // 2.销毁----此处不能销毁,因为任务没有提交执行完,如果销毁线程池,任务也就无法执行了
        // exec.destory();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
