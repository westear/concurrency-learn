package com.westear.concurrency.javaOriginUtils.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * 同步屏障：让一组线程到达一个屏障（同步点）时被阻塞，直到最后一个线程到达屏障时。屏障才会开门,所有被屏障的线程会继续运行
 * 该程序结果可能是 1,2; 也可能是 2,1
 * 如果new CyclicBarrier(3); 则主线程和子线程会永远等待，因为没有第三个线程执行await()方法
 */
public class CyclicBarrierTest {

    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) {
        new Thread(()->{
            try {
                cyclicBarrier.await();
            } catch (Exception e) {

            }
            System.out.println(1);
        }).start();

        try {
            cyclicBarrier.await();
        } catch (Exception e) {

        }
        System.out.println(2);
    }
}
