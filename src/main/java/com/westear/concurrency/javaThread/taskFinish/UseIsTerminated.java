package com.westear.concurrency.javaThread.taskFinish;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 多任务并发：如何判断线程池中的任务都已经执行完毕？
 *
 * 使用线程池的原生函数isTerminated();
 *
 *  优点：操作简便；
 *  缺点：需要主线程阻塞；
 */
public class UseIsTerminated {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(7);
        for (int i = 0; i < 10; i++) {
            AnyTask anyTask = new AnyTask();
            executor.execute(anyTask);
        }
        //顺序关闭，不再接受新任务
        executor.shutdown();

        //主线程阻塞，后续怎么停止主线程是个问题
        while (true) {
            //所有在执行的任务在线程池关闭后执行结束，则返回true
            if(executor.isTerminated()) {
                EndTask endTask = new EndTask();
                endTask.taskRun();
            } else {
                TimeUnit.SECONDS.sleep(3);
            }

        }
    }
}

class AnyTask implements Runnable {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(3);
            System.out.println(Thread.currentThread().getName()+": 普通任务执行执行完毕！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class EndTask {
    public void taskRun() {
        try {
            System.out.println("开始执行最终任务");

        }catch(Exception e) {
            e.printStackTrace();

        }
    }
}
