package com.westear.concurrency.javaThread.taskFinish;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 多任务并发：如何判断线程池中的任务都已经执行完毕？
 *  优点：操作相对简便，可以把等待线程池中任务完成后的后续工作做成任务，同样放到线程池中运行，就是可以控制线程池中任务执行的顺序。
 *  缺点：需要提前知道任务的数量。
 *
 *  原理：其工作原理是赋给CountDownLatch一个计数值，普通的任务执行完毕后，调用countDown()执行计数值减一。
 *       最后执行的任务在调用方法的开始调用await()方法，这样整个任务会阻塞，直到这个计数值（Count）为零，才会继续执行。
 */
public class UseCountDownLatch {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(7);
        //需要知道任务数量
        CountDownLatch taskLatch = new CountDownLatch(10);
        LastTask lastTask = new LastTask(taskLatch);

        executor.execute(lastTask);

        for(int i=0;i<10;i++) {
            executor.execute(new RunTask(taskLatch));
        }

        //顺序关闭，不再接受新任务
        executor.shutdown();

        System.out.println("主线程已经执行完了");
    }
}

class RunTask implements Runnable{

    private CountDownLatch endTaskLatch;

    public RunTask(CountDownLatch latch) {

        this.endTaskLatch = latch;

    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(10);
            System.out.println(Thread.currentThread().getName()+": 普通任务执行执行完毕！");
            endTaskLatch.countDown();
            System.out.println(endTaskLatch.getCount());

        }catch(Exception e) {

            e.printStackTrace();

        }
    }
}

class LastTask implements Runnable{

    private CountDownLatch endTaskLatch;

    LastTask(CountDownLatch latch) {

        this.endTaskLatch =latch;
    }

    public void run() {

        try {

            endTaskLatch.await();
            System.out.println("开始执行最终任务");
            System.out.println(endTaskLatch.getCount());
        }catch(Exception e) {
            e.printStackTrace();

        }
    }
}