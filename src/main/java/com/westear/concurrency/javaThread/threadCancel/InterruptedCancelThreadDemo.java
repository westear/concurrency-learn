package com.westear.concurrency.javaThread.threadCancel;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 线程中断是一种协作机制，线程可以通过这种机制来通知另一个线程，告诉它在合适的或者可能情况下停止当前工作。
 *
 * 虽然线程的取消和中断没有必然联系，但是在实践中发现：中断是实现取消的最合理方式。
 *
 * 对中断操作的正确理解是：它并不会真正的中断线程，而是给线程发出中断通知，告知目标线程有人希望你退出。
 *
 * 目标线程收到通知后如何处理完全由目标线程自行决定，这是非常重要的。线程收到中断通知后，通常会在下一个合适的时刻(被称为取消点)中断自己。
 * 有些方法，如wait、sleep和join等将严格地处理这种请求，当它们收到中断请求或者在开始执行时发现某个已被设置好的中断状态时，将抛出一个异常。
 *
 * 对于前面BrokenPrimeProducer的问题很容易解决(和简化)，使用中断而不是boolean标识来请求取消。
 *      第一次是在循环开始前，显示检查中断请求；
 *      第二次是在put方法，该方法为阻塞的，会隐式的检测当前线程是否被中断；
 */
public class InterruptedCancelThreadDemo {

    private static class BrokenPrimeProducer implements Runnable {

        private final BlockingQueue<BigInteger> queue;

        BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                BigInteger p = BigInteger.ONE;
                //检查中断
                while (!Thread.currentThread().isInterrupted())
                    //put 存在隐式中断请求的检查
                    queue.put(p = p.nextProbablePrime());
                System.out.println("当前线程已中断");
            } catch (InterruptedException e) {
                /* 允许线程退出 */
                // Thread.currentThread().interrupt() 我们把这句代码去掉，运行你会发现这个线程无法终止，
                // 因为在抛出InterruptedException 的同时，线程的中断标志被清除了，
                // 所以在while语句中判断当前线程是否中断时，返回的是false。
                Thread.currentThread().interrupt();
                System.out.println("当前线程响应中断抛出异常：" + Thread.currentThread().isInterrupted());
            }
        }

        //对当前线程发出中断信号
        public void cancel() {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<BigInteger> queue = new LinkedBlockingDeque<>();
        BrokenPrimeProducer brokenPrimeProducer = new BrokenPrimeProducer(queue);
        new Thread(brokenPrimeProducer).start();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {

        }finally {
            brokenPrimeProducer.cancel();
        }
        while (!queue.isEmpty()) {
            System.out.println(queue.take());
        }
    }
}
