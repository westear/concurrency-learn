package com.westear.concurrency.blockingQueueOrDeque;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue : 一个支持延时获取元素的无界阻塞队列。队列使用了 PriorityBlockingQueue 来实现。
 * 队列中的元素必须实现Delayed接口, 容器里每一个元素都设置了一个时间，时间到了才能从中提取元素
 * 代码实现可以参考
 *      java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask#ScheduledFutureTask(java.lang.Runnable, java.lang.Object, long)
 *      java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask#ScheduledFutureTask(java.lang.Runnable, java.lang.Object, long, long)
 *      java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask#ScheduledFutureTask(java.util.concurrent.Callable, long)
 *
 *      java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask#getDelay(java.util.concurrent.TimeUnit)
 *      java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask#compareTo(java.util.concurrent.Delayed)
 * 使用场景：
 *  1.缓存系统的设计
 *  2.定时任务调度
 */
public class DelayQueueDemo {

    private static BlockingQueue<DelayQueueDemo.MyTask> tasks = new DelayQueue<>();

    /**
     * 队列中的元素必须实现Delayed接口, 创建元素时可以指定多久才能从队列中获取当前元素，只有在延迟期满时才能从队列中提取元素。
     */
    static class MyTask implements Delayed {

        long runningTime;

        public MyTask(long rt) {
            this.runningTime = rt;
        }

        /**
         * 指定元素顺序，根据延时时间排序
         * @param o 队列元素
         * @return 比较结果
         */
        @Override
        public int compareTo(Delayed o) {
            if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MICROSECONDS)) {
                return -1;
            }else if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
                return 1;
            }else {
                return 0;
            }
        }

        /**
         * 该方法返回当前元素还需要延时多长时间，单位纳秒
         * @param unit 时间单位
         * @return 当前元素还需要延时多长时间
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(runningTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public String toString() {
            return "" + runningTime;
        }

        public static void main(String[] args) throws InterruptedException {
            long now = System.currentTimeMillis();
            DelayQueueDemo.MyTask t1 = new DelayQueueDemo.MyTask(now+1000);
            DelayQueueDemo.MyTask t2 = new DelayQueueDemo.MyTask(now+2000);
            DelayQueueDemo.MyTask t3 = new DelayQueueDemo.MyTask(now+1500);
            DelayQueueDemo.MyTask t4 = new DelayQueueDemo.MyTask(now+2500);
            DelayQueueDemo.MyTask t5 = new DelayQueueDemo.MyTask(now+500);

            tasks.put(t1);
            tasks.put(t2);
            tasks.put(t3);
            tasks.put(t4);
            tasks.put(t5);

            System.out.println(tasks);

            for (int i = 0; i < 5; i++) {
                System.out.println(tasks.take());
            }
        }

    }
}
