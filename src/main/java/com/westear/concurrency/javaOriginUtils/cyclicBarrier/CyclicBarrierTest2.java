package com.westear.concurrency.javaOriginUtils.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * 设置拦截数=2， 所以必须等待代码中的第一个线程和线程A都执行完，才会继续执行主线程，然后输出2
 */
public class CyclicBarrierTest2 {

    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new A());

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

    static class A implements Runnable {

        @Override
        public void run() {
            System.out.println(3);
        }
    }
}
