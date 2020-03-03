package com.westear.concurrency.lockAndAQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 启动10个线程，但只有线程0和1名称成对输出，也就是说同一时刻只有两个线程能够获取到锁, 多余的线程始终无法获取锁
 */
public class TwinsLockTest {

    public static void main(String[] args) {
        final Lock lock = new TwinsLock();

        class Worker extends Thread {
            @Override
            public void run() {
                while (true) {
                    lock.lock();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(Thread.currentThread().getName());
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {

                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

        //启动是10个线程
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker();
            worker.setName("Thread-"+i);
            worker.setDaemon(true);
            worker.start();
        }

        //每隔1秒换行
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {

            }
            System.out.println(Thread.currentThread().getName());
        }
    }


}
