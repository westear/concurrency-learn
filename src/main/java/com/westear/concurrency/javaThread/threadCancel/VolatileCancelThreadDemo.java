package com.westear.concurrency.javaThread.threadCancel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * 使用volatile状态变量来控制
 * 操作被取消的原因有很多，比如超时，异常，请求被取消等等
 * 一个可取消的任务要求必须设置取消策略，即如何取消，何时检查取消命令，以及接收到取消命令之后如何处理
 *
 * 最简单的取消办法就是利用取消标志位，如下所示。这段代码用于生成素数，并在任务运行一秒钟之后终止。
 * 其取消策略为：通过改变取消标志位取消任务，任务在每次生成下一随机素数之前检查任务是否被取消，被取消后任务将退出。
 *
 * ，该机制最大的问题：无法应用于阻塞方法 例如BlockingQueue.put()
 *       可能会产生一个很严重的问题——任务可能因阻塞而永远无法检查取消标识，导致任务永远不会结束。
 */
public class VolatileCancelThreadDemo {

    private static ExecutorService exec = Executors.newCachedThreadPool();

    private static class PrimerGenerator implements Runnable {

        private List<BigInteger> primers;
        private volatile boolean cancelled;

        public PrimerGenerator(List<BigInteger> primers, boolean cancelled) {
            this.primers = primers;
            this.cancelled = cancelled;
        }
        @Override
        public void run() {
            BigInteger p = BigInteger.ONE;
            //检查状态，从而取消任务
            while (!cancelled) {
                p = p.nextProbablePrime();
                synchronized (this) {
                    primers.add(p);
                }
            }
        }

        public void cancel() {
            cancelled = true;
        }

        public synchronized List<BigInteger> get() {
            return new ArrayList<>(primers);
        }

    }

    private static class BrokenPrimeProducer implements Runnable {
        private final BlockingQueue<BigInteger> queue;
        private volatile boolean cancelled = false;

        BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                BigInteger p = BigInteger.ONE;
                while (!cancelled)
                    //这里可能产生阻塞，从而无法取消任务。
                    queue.put(p = p.nextProbablePrime());
            } catch (InterruptedException consumed) {
            }
        }

        public void cancel() {
            cancelled = true;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ArrayList<BigInteger> list = new ArrayList<>();
        PrimerGenerator generator = new PrimerGenerator(list, false);
        exec.execute(generator);
        try {
            MILLISECONDS.sleep(500);
        } finally {
            generator.cancel();
//            exec.shutdown();
        }
        List<BigInteger> bigIntegers = generator.get();
        for (BigInteger i: bigIntegers) {
            System.out.println(i);
        }


        BlockingQueue<BigInteger> queue = new LinkedBlockingDeque<>();
        BrokenPrimeProducer brokenPrimeProducer = new BrokenPrimeProducer(queue);
        exec.execute(brokenPrimeProducer);
        try {
            MILLISECONDS.sleep(500);
        } finally {
            brokenPrimeProducer.cancel();
            exec.shutdown();
        }
        while (!queue.isEmpty()) {
            System.out.println(queue.take());
        }
        exec.shutdown();
    }

}
