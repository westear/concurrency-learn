package com.westear.concurrency.lockAndAQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 的使用
 * 写读互斥
 * 一个线程获取了写锁时，不允许另一个线程占有对应的读锁
 */
public class WriteReadRejectDemo {

    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static Lock readLock = reentrantReadWriteLock.readLock();
    private static Lock writeLock = reentrantReadWriteLock.writeLock();

    private int i;

    public void read() {
        //获取读锁
        readLock.lock();
        try {
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " 获取读锁; i = " + i);
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放读锁
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " 释放读锁; i = " + i);
            readLock.unlock();
        }
    }

    public void write() {
        //占有写锁
        writeLock.lock();
        try {
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " 占有写锁; i = " + i);
            TimeUnit.SECONDS.sleep(2);
            i++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放写锁
            System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " 释放写锁; i = " + i);
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        final WriteReadRejectDemo writeReadRejectDemo = new WriteReadRejectDemo();

        //写
        new Thread(writeReadRejectDemo::write, "writeThread").start();

        //读
        new Thread(writeReadRejectDemo::read, "readThread").start();

    }
}
