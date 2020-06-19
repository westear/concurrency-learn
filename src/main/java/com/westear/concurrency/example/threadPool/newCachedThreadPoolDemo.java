package com.westear.concurrency.example.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class newCachedThreadPoolDemo {

    public static void main(String[] args) {

        /**
         * corePool == 0
         * 缓冲队列长度==0， 入队后马上出队的 SynchronousQueue
         * maximumPoolSize = Integer.MAX_VALUE，可以认为是无限大
         * 所以可以无限创建线程，在资源有限的情况下容易引起OOM异常
         * 不推荐使用
         */
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    log.info("task:{}", index);
                }
            });
        }
        executorService.shutdown();
    }
}
