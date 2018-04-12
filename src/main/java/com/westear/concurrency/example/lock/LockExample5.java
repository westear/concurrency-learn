package com.westear.concurrency.example.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * Condition demo
 */
@Slf4j
public class LockExample5 {

	public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        new Thread(() -> {
            try {
                reentrantLock.lock();	//获得锁进入AQS线程队列
                log.info("{} wait signal",Thread.currentThread().getId()); // 1	执行线程
                condition.await();		//该线程释放锁，该线程进入condition线程队列
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("{} get signal",Thread.currentThread().getId()); // 4 又获得锁执行线程
            reentrantLock.unlock();	//释放锁
        }).start();

        new Thread(() -> {
            reentrantLock.lock();	//该线程获得锁进入AQS线程队列
            log.info("{} get lock",Thread.currentThread().getId()); // 2 执行线程
            try {
                Thread.sleep(3000);	// 执行线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition.signalAll();	//发送信号唤醒等待线程，使它们进入AQS线程队列
            log.info("{} send signal ~ ",Thread.currentThread().getId()); // 3 
            reentrantLock.unlock();	//释放锁
        }).start();
    }
}
