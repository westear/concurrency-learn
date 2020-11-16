package com.westear.concurrency.javaOriginUtils.countDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * 如果想等待N个线程完成再执行主线程，参数传入N
 */
public class CountDownLatchTest {
    //N = 2
    static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) {
        new Thread(()->{
            System.out.println(1);
            //调用countDown方法时，N会减1，await()方法会阻塞当前线程,直到N=0,唤醒
            countDownLatch.countDown();
            System.out.println(2);
            countDownLatch.countDown();
        }).start();
        System.out.println("3");
    }
}
