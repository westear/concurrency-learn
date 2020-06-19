package com.westear.concurrency.lockAndAQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 的使用
 * 读锁和读锁共享
 * 多个线程同一时刻获取到读锁
 */
public class ReadShareDemo {

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static Lock readLock = reentrantReadWriteLock.readLock();

    private int i;

    public void read() {
        //读锁加锁
        readLock.lock();
        try {
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " 占有读锁; i = " + i);
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放读锁
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " 释放读锁; i = " + i);
            readLock.unlock();
        }
    }

    public static void main(String[] args) {
        final ReadShareDemo readShareDemo = new ReadShareDemo();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                readShareDemo.read();
            }
        };

        new Thread(runnable,"thread1").start();
        new Thread(runnable,"thread2").start();
        new Thread(runnable,"thread3").start();
    }
}
