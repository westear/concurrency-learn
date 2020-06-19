package com.westear.concurrency.example.threadPool;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class newScheduledThreadPoolDemo {

    public static void main(String[] args) {

        /**
         * 核心线程数由构造器传入
         * 最大线程数是Integer.MAX_VALUE
         * DelayQueue是一个没有大小限制的队列, 如果一直生产却没有消费， 有可能造成OOM
         */
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

//        executorService.schedule(new Runnable() {
//            @Override
//            public void run() {
//                log.warn("schedule run");
//            }
//        }, 3, TimeUnit.SECONDS);	//每隔3秒执行一个线程

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.warn("schedule run");
            }
        }, 1, 3, TimeUnit.SECONDS);	//延迟1秒，每隔3秒执行一个线程
//        executorService.shutdown();

        //定时执行类
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.warn("timer run");
            }
        }, new Date(), 5 * 1000);	//每次间隔5秒继续执行
    }
}