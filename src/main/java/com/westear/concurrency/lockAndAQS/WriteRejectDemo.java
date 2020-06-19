package com.westear.concurrency.lockAndAQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 的使用
 * 写写互斥
 * 写锁被一个线程占有时，不允许其他线程占有写锁
 */
public class WriteRejectDemo {

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static Lock writeLock = reentrantReadWriteLock.writeLock();

    private int i;

    public void write() {
        //获取写锁
        writeLock.lock();
        try {
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " 占有写锁; i = " + i);
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //释放写锁
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " 释放写锁; i = " + i);
            writeLock.unlock();
            i++;
        }
    }

    public static void main(String[] args) {
        final WriteRejectDemo writeRejectDemo = new WriteRejectDemo();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                writeRejectDemo.write();
            }
        };

        new Thread(runnable,"thread1").start();
        new Thread(runnable,"thread2").start();
        new Thread(runnable,"thread3").start();
    }

}
