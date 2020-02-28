package com.luban.aqs;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * CyclicBarrier 线程屏障的应用, 注意与 CountDownLatch 的区别
 */
public class RaceDemo {

    public static void main(String[] args) {
        CyclicBarrier barrier=new CyclicBarrier(8);
        Thread[] play=new Thread[8];
        for (int i = 0; i < 8; i++) {
            play[i]=new Thread(()->{
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                    System.out.println(Thread.currentThread().getName()+"准备好了");
                    //等待剩下的线程准备好之前，该线程在此先进入阻塞
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //所有线程准备完毕，开始执行阻塞后的语句
                System.out.println("选手"+Thread.currentThread().getName()+"起跑");
            },"play["+i+"]");
            play[i].start();
        }
    }
}
