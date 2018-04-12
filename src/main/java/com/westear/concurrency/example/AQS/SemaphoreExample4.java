package com.westear.concurrency.example.AQS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SemaphoreExample4 {

	private final static int threadCount = 20;

    public static void main(String[] args) throws Exception {

        ExecutorService exec = Executors.newCachedThreadPool();

        final Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            exec.execute(() -> {
            	try {
                    if (semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS)) { // 如果没有获取，尝试等待5000毫秒
                        test(threadNum);
                        semaphore.release(); // 释放一个许可
                    }else{
                    	log.info("tryAcquire false:{} ",Thread.currentThread().getId());
                    }
                } catch (Exception e) {
                    log.error("exception", e);
                }
            });
        }
        exec.shutdown();
    }

    private static void test(int threadNum) throws Exception {
        log.info("{}", threadNum);
        Thread.sleep(1000);
    }
}
