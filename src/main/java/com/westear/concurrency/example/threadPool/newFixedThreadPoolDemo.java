package com.westear.concurrency.example.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class newFixedThreadPoolDemo {

    public static void main(String[] args) {

        /**
         * 核心线程数和最大线程数都是构造时传入的线程数
         * 由于使用的是LinkedBlockingQueue，在资源有限的时候容易引起OOM异常
         */
        ExecutorService executorService = Executors.newFixedThreadPool(3);

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
