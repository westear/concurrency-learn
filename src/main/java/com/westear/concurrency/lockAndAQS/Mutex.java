package com.westear.concurrency.lockAndAQS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 该程序，独占锁 Mutex 是一个自定义同步组件，它在同一时刻只允许一个线程占有锁。
 */
public class Mutex implements Lock {

    /**
     * 静态内部类，自定义同步器
     * 内部类继承了同步器，实现了独占式获取锁和释放同步状态.
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        //是否处于占用状态
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        //当状态为0的时候获取锁
        @Override
        public boolean tryAcquire(int acquires) {
            if(compareAndSetState(0, 1)) {
                //设置当前线程独占,状态设置为1
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        //释放锁，将状态设置为0
        @Override
        protected boolean tryRelease(int release) {
            //state = 0，未持有，不需要释放
            if(getState() == 0) {
                throw new IllegalArgumentException();
            }
            //独占设置为空
            setExclusiveOwnerThread(null);
            //将状态设置为0
            setState(0);
            return true;
        }

        //返回一个Condition， 每个 condition 都包含了一个 condition 队列
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    //仅需要将操作代理到sync上即可
    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
