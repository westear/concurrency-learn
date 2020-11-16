package com.westear.concurrency.futureAndCallable;

import java.util.concurrent.*;

/**
 * Callable和Runnable一样代表着任务，区别在于Callable有返回值并且可以抛出异常。其使用如下：
 */
public class CallableDemo {

    static class SumTask implements Callable<Long> {

        @Override
        public Long call() throws Exception {

            long sum = 0;
            for (int i = 0; i < 9000; i++) {
                sum += i;
            }

            return sum;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Start:" + System.nanoTime());
        /*
         * FutureTask类实现了RunnableFuture接口，而RunnableFuture接口继承了Runnable和Future接口，所以说FutureTask是一个提供异步计算的结果的任务。
         * FutureTask可以用来包装Callable或者Runnable对象。因为FutureTask实现了Runnable接口，所以FutureTask也可以被提交给Executor
         */
        FutureTask<Long> futureTask = new FutureTask<>(new SumTask());
        Executor executor= Executors.newSingleThreadExecutor();
        executor.execute(futureTask);

        //获取执行结果， get方法可以允许多个线程调用
//        System.out.println(futureTask.get());
        for(int i=0;i<5;i++){
            executor.execute(() -> {
                try {
                    System.out.println("get result "+futureTask.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println("End:" + System.nanoTime());
    }

}
