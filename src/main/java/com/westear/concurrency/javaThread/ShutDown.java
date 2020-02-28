package com.westear.concurrency.javaThread;

import java.util.concurrent.TimeUnit;

/**
 * 优雅安全地终止线程示例，不推荐使用 suspend(), resume(), stop()，因为这些方法不会释放占有的资源，容易造成死锁问题
 */
public class ShutDown {
    public static void main(String[] args) throws Exception {
        Runner one = new Runner();
        Thread countThread = new Thread(one, "CountThread");
        countThread.start();
        //睡眠1秒，main 线程对CountThread进行中断，使countThread能够感知中断而结束
        TimeUnit.SECONDS.sleep(1);
        countThread.interrupt();

        Runner two = new Runner();
        countThread = new Thread(two, "CountThread");
        countThread.start();
        //睡眠1秒，main 线程对 Runner two 进行取消，使countThread能够感知 on 为 false 而结束
        TimeUnit.SECONDS.sleep(1);
        two.cancel();
    }

    private static class Runner implements Runnable {
        private long i;
        private volatile boolean on = true;

        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                i++;
            }
            System.out.println("Count i = " + i);
        }

        public void cancel() {
            on = false;
        }
    }
}
