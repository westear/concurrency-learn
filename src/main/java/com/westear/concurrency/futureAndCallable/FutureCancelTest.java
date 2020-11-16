package com.westear.concurrency.futureAndCallable;

import java.util.concurrent.*;

/**
 * Future 的 cancel 方法本质上是调用了 interrupt() 方法使得线程发生中断
 */
public class FutureCancelTest {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
//        SimpleTask simpleTask = new SimpleTask(3_000);
//        Future<Double> future = executor.submit(simpleTask);

        long num = 1000000033L;
        PrimerTask primerTask = new PrimerTask(num);
        Future<Boolean> future = executor.submit(primerTask);

        executor.shutdown();

//        cancelTask(future, 2_000); //任务运行两秒后取消该任务

        //虽然取消了任务，Future 也对取消作出了响应（抛出了 CancellationException 异常）， 但任务没有立即停止，程序等到任务执行完成后才停止
        //必须在任务中通过 isInterrupted() 方法判断当前是否中断成功，成功中断则跳出循环结束任务线程
        cancelPrimerTask(future, 2_000);  //任务运行两秒后取消该任务

        try {
//            double time = future.get();
//            System.out.format("任务运行时间：%.3f s\n", time);

            boolean isPrimer = future.get();
            System.out.format("%d 是否为素数？ %b\n", num, isPrimer);
        } catch (CancellationException ex) {
            System.err.println("任务被取消: " + future.isCancelled());
        } catch (InterruptedException ex) {
            System.err.println("当前线程被中断");
        } catch (ExecutionException ex) {   // 任务异常，重新抛出异常信息
            System.err.println("任务执行出错");
        }
    }

    private static final class SimpleTask implements Callable<Double> {

        private final int sleepTime;

        public SimpleTask(int sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public Double call() throws Exception {
            double begin = System.nanoTime();
            Thread.sleep(sleepTime);
            double end = System.nanoTime();
            return (end - begin) / 1E9;
        }
    }

    private static final class PrimerTask implements Callable<Boolean> {

        private final long num;

        public PrimerTask(long num) {
            this.num = num;
        }

        @Override
        public Boolean call() throws Exception {
            for (int i = 2; i < num; i++) {
                if (Thread.currentThread().isInterrupted()) {   //如果任务被取消，跳出循环
                    System.out.println("PrimerTask 任务被取消");
                    return false;
                }

                if (num % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 取消任务的方法
     * @param future Future<Double>
     * @param delay int
     */
    private static void cancelTask(final Future<Double> future, final int delay) {
        //启动一个线程取消任务
        Runnable cancellation = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    future.cancel(true); //取消与 future 关联的正在运行的任务
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(cancellation).start();
    }

    /**
     * 用于取消 PrimerTask 任务的方法
     * @param future Future<Boolean>
     * @param delay int
     */
    private static void cancelPrimerTask(final Future<Boolean> future, int delay) {
        //启动一个线程取消任务
        Runnable cancellation = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                    future.cancel(true); //取消与 future 关联的正在运行的任务
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(cancellation).start();
    }
}
