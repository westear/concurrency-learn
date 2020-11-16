package com.westear.concurrency.javaThread.taskFinish;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多任务并发：如何判断线程池中的任务都已经执行完毕？
 *
 * 使用重入锁，维持一个公共计数,
 * 实行起来基本上和 CountDownLatch 的原理差不多
 *
 *  优点：灵活、可控
 *  缺点：需要预知运算的任务数、操作相对有点繁琐
 */
public class UseCount {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(7);

        int taskNum = 10;

        for(int i=0;i<taskNum;i++) {

            AnyTask1 anyTask = new AnyTask1();
            executor.execute(anyTask);

        }
        EndTask1 endTask = new EndTask1(taskNum);
        executor.execute(endTask);
        executor.shutdown();
    }
}


class AnyTask1 implements Runnable{

    public static int taskNum = 0;
    private Lock lock = new ReentrantLock();

    public void run() {
        try {
            Thread.sleep(3000);

            System.out.println(Thread.currentThread().getName()+": 普通任务执行执行完毕！");

            lock.lock();
            AnyTask1.taskNum++;
            lock.unlock();
        }catch(Exception e) {

            e.printStackTrace();

        }
    }
}

class EndTask1 implements Runnable{

    private int tnum = 0;
    public EndTask1(int tnum) {
        this.tnum = tnum;

    }

    public void run() {

        try {
            while(true) {
                if(tnum==AnyTask1.taskNum) {

                    System.out.println("开始执行最终任务");
                    break;

                }else {
                    System.out.println("线程池的任务没有完成，等待。。。");
                    Thread.sleep(3000);
                }
            }

        }catch(Exception e) {
            e.printStackTrace();

        }
    }
}
