package com.westear.concurrency.javaThread;

import java.util.concurrent.TimeUnit;

/**
 * 优雅安全地终止线程示例，不推荐使用 suspend(), resume(), stop()，因为这些方法不会释放占有的资源，容易造成死锁问题
 *
 * 当线程进入 runnable 状态之后，通过设置一个标识位，线程在合适的时机，检查该标识位，发现符合终止条件，自动退出 run () 方法，线程终止。
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
        private volatile boolean on = true; //volatile 确保标志变量的可见性, 能被当前执行线程读取到最新的状态

        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                System.out.println("当前线程正在执行......");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    //JVM 的异常处理会清除线程的中断状态, 线程的 isInterrupted() 方法调用仍等于 false, 显示未中断
                    //可以在异常捕获块中重新设置中断状态
                    System.out.println("当前线程被中断......");
                    Thread.currentThread().interrupt();
                }
            }

        }

        public void cancel() {
            on = false;
        }
    }
}
