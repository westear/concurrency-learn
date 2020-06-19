package com.westear.concurrency.example.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class newSingleThreadExecutorDemo {

    public static void main(String[] args) {

        /**
         * 核心线程数和最大线程数均为1
         * 阻塞队列为LinkedBlockingQueue，在资源有限的时候容易引起OOM异常
         */
        ExecutorService executorService = Executors.newSingleThreadExecutor();

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
