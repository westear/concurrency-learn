package com.westear.concurrency.lockAndAQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 共享锁,最多允许两个线程获得锁资源
 */
public class TwinsLock implements Lock {
    private final Sync sync = new Sync(2);

    private static final class Sync extends AbstractQueuedSynchronizer {
        Sync(int count) {
            if(count <= 0) {
                throw new IllegalArgumentException("count must large than zero.");
            }
            setState(count);
        }

        @Override
        protected int tryAcquireShared(int reduceCount) {
            for (;;) {
                int current = getState();
                int newCount = current - reduceCount;
                if(newCount < 0 || compareAndSetState(current, newCount)) {
                    return newCount;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int returnCount) {
            for (;;) {
                int current = getState();
                int newCount = current + returnCount;
                if(compareAndSetState(current, newCount)) {
                    return true;
                }
            }
        }

        @Override
        protected boolean tryAcquire(int arg) {
            if(arg < 0 || arg > 2) {
                throw new IllegalArgumentException("arg illegal");
            }
            return super.tryAcquire(arg);
        }

        @Override
        protected boolean tryRelease(int arg) {
            if(arg < 0 || arg > 2) {
                throw new IllegalArgumentException("arg illegal");
            }
            return super.tryRelease(arg);
        }

        //返回一个Condition， 每个 condition 都包含了一个 condition 队列
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    @Override
    public void lock() {
        sync.acquireShared(1);
    }

    @Override
    @Deprecated
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    @Deprecated
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    @Deprecated
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
