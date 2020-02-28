package com.westear.concurrency.javaThread;

import java.util.concurrent.TimeUnit;

/**
 * 本程序先创建了两个线程 SleepThread 和 BusyThread,
 * 前者不停地睡眠， 后者一直运行，然后对这两个线程分别进行中断操作，观察二者的中断标识位
 * 可以看出抛出 InterruptedException 的线程 sleepThread，其中断标识位被清除了，
 * 而一直忙碌运作的线程 busyThread，中断标识位没有被清除
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
