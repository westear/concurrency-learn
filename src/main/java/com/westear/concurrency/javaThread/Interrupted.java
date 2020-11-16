package com.westear.concurrency.javaThread;

import java.util.concurrent.TimeUnit;

/**
 * 本程序先创建了两个线程 SleepThread 和 BusyThread,
 * 前者不停地睡眠， 后者一直运行，然后对这两个线程分别进行中断操作，观察二者的中断标识位
 * 可以看出抛出 InterruptedException 的线程 sleepThread，其中断标识位被清除了，
 * 而一直忙碌运作的线程 busyThread，中断标识位没有被清除
 *
 * 线程只有从 runnable 状态（可运行/运行状态） 才能进入terminated 状态（终止状态），
 * 如果线程处于 blocked、waiting、timed_waiting 状态（休眠状态），
 * 就需要通过 Thread 类的 interrupt()  方法，让线程从休眠状态进入 runnable 状态，从而结束线程。
 */
public class Interrupted {
    public static void main(String[] args) throws Exception {
        // sleepThread不停地尝试睡眠
        Thread sleepThread = new Thread(new SleepRunner());
        sleepThread.setDaemon(true);
        //busyThread不停地运行
        Thread busyThread = new Thread(new BusyRunner());
        busyThread.setDaemon(true);

        sleepThread.start();
        busyThread.start();

        //休眠5秒，让sleepThread和busyThread充分进行
        TimeUnit.SECONDS.sleep(5);
        sleepThread.interrupt();
        busyThread.interrupt();

        System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());
        //防止sleepThread和busyThread立刻退出
        TimeUnit.SECONDS.sleep(2);
    }

    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e){
                    //JVM 的异常处理会清除线程的中断状态, 线程的 isInterrupted() 方法调用仍等于 false, 显示未中断
                    //可以在异常捕获块中重新设置中断状态
                }
            }
        }
    }

    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {

            }
        }
    }
}
