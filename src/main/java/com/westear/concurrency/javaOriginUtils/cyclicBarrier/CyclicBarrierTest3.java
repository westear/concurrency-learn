package com.westear.concurrency.javaOriginUtils.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * 结果返回true, 说明阻塞的线程已被中断
 */
public class CyclicBarrierTest3 {

    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.interrupt();

        try {
            cyclicBarrier.await();
        } catch (Exception e) {
            System.out.println(cyclicBarrier.isBroken());
        }
    }
}
