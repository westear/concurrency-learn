package com.westear.concurrency.lockAndAQS;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试观测公平锁和非公平锁在获取锁时候的区别
 */
public class FairAndUnfairTest {

    private static Lock fairLock = new ReentrantLock2(true);
    private static Lock unfairLock = new ReentrantLock2(false);

    //启动5个Job
    private void testLock(String type, Lock lock) throws InterruptedException {
        System.out.println(type);
        for (int i = 0; i < 5; i++) {
            Job job = new Job(type+i, lock);
            job.start();
        }
        Thread.sleep(11000);
    }


    @Test
    public void unfair() throws InterruptedException {
        testLock("非公平锁", unfairLock);
    }

    @Test
    public void fair() throws InterruptedException {
        testLock("公平锁", fairLock);
    }

    private static class Job extends Thread {
        private Lock lock;

        public Job(String lockType, Lock lock) {
            this.setName(lockType);
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                lock.lock();
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("获取锁的当前线程[" + Thread.currentThread().getName() + "], " +
                            "同步队列中的线程" + ((ReentrantLock2)lock).getQueuedThreads() + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 重新实现ReentrantLock类是为了重写getQueuedThreads方法，便于试验的观察
     */
    private static class ReentrantLock2 extends ReentrantLock {
        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        @Override
        public Collection<Thread> getQueuedThreads() {
            List<Thread> arrayList = new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(arrayList);
            return arrayList;
        }
    }
}
