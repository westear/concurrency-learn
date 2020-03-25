package com.westear.concurrency.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * public class BumThread extends Thread{
 *
 *     public void run() {
 *         try {
 *             Thread.sleep(1000*3);
 *             System.out.println("包子准备完毕");
 *         } catch (InterruptedException e) {
 *             e.printStackTrace();
 *         }
 *     }
 *
 * }
 *
 * public class ColdDishThread extends Thread{
 *
 *     public void run() {
 *         try {
 *             Thread.sleep(1000);
 *             System.out.println("凉菜准备完毕");
 *         } catch (InterruptedException e) {
 *             e.printStackTrace();
 *         }
 *     }
 *
 * }
 *
 * public static void main(String[] args) throws InterruptedException {
 *      long start = System.currentTimeMillis();
 *
 *      // 等凉菜 -- 必须要等待返回的结果，所以要调用join方法
 *      Thread t1 = new ColdDishThread();
 *      t1.start();
 *      t1.join();
 *
 *      // 等包子 -- 必须要等待返回的结果，所以要调用join方法
 *      Thread t2 = new BumThread();
 *      t2.start();
 *      t2.join();
 *
 *      long end = System.currentTimeMillis();
 *      System.out.println("准备完毕时间："+(end-start));
 * }
 *
 * 使用Future模式的示例:
 *
 * Future接口:
 *      Future是一个接口，代表了一个异步计算的结果。接口中的方法用来检查计算是否完成、等待完成和得到计算的结果。
 *      当计算完成后，只能通过get()方法得到结果，get方法会阻塞直到结果准备好了。如果想取消，那么调用cancel()方法。
 *      其他方法用于确定任务是正常完成还是取消了。一旦计算完成了，那么这个计算就不能被取消。
 *
 * FutureTask类:
 *      FutureTask类实现了RunnableFuture接口，而RunnableFuture接口继承了Runnable和Future接口，所以说FutureTask是一个提供异步计算的结果的任务。
 *      FutureTask可以用来包装Callable或者Runnable对象。因为FutureTask实现了Runnable接口，所以FutureTask也可以被提交给Executor
 *
 * FutureTask的状态,FutureTask中有一个表示任务状态的int值，初始为NEW。定义如下：
 *     private volatile int state;
 *     private static final int NEW          = 0;
 *     private static final int COMPLETING   = 1;
 *     private static final int NORMAL       = 2;
 *     private static final int EXCEPTIONAL  = 3;
 *     private static final int CANCELLED    = 4;
 *     private static final int INTERRUPTING = 5;
 *     private static final int INTERRUPTED  = 6;
 *
 * 可能的状态转换包括：
 * - NEW -> COMPLETING -> NORMAL
 * - NEW -> COMPLETING -> EXCEPTIONAL
 * - NEW -> CANCELLED
 * - NEW -> INTERRUPTING -> INTERRUPTED
 */
public class FutureDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        // 等凉菜
        Callable<String> ca1 = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "凉菜准备完毕";
        };
        FutureTask<String> ft1 = new FutureTask<>(ca1);
        new Thread(ft1).start();

        // 等包子 -- 必须要等待返回的结果
        Callable<String> ca2 = () -> {
            try {
                Thread.sleep(1000*3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "包子准备完毕";
        };
        FutureTask<String> ft2 = new FutureTask<>(ca2);
        new Thread(ft2).start();

        System.out.println(ft1.get());
        System.out.println(ft2.get());

        long end = System.currentTimeMillis();
        System.out.println("准备完毕时间："+(end-start));
    }
}
