package com.westear.concurrency.example.AQS;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用场景：启动多线程处理任务，等待所有任务结束后进行下一步处理
 * @author SAGE
 *
 */

@Slf4j
public class CountDownLatchExample2 {

	private final static int threadCount = 200;

    public static void main(String[] args) throws Exception {

        ExecutorService exec = Executors.newCachedThreadPool();

      //只能设置一次，且不允许修改
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            exec.execute(() -> {
                try {
                    test(threadNum);
                } catch (Exception e) {
                    log.error("exception", e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await(10, TimeUnit.MILLISECONDS); //从第一个线程调用开始之后10毫秒退出等待
        log.info("finish");
        exec.shutdown();	//并非马上清除剩余所有进入线程池的线程，而是此刻禁止新线程加入，并执行完已存在线程
    }

    private static void test(int threadNum) throws Exception {
        Thread.sleep(100);	//模拟任务处理时间
        log.info("{}", threadNum);
        Thread.sleep(100);
    }
}
