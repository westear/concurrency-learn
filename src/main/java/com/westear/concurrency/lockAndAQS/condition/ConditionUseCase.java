package com.westear.concurrency.lockAndAQS.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition 定义了等待/通知两种类型的方法，当前线程调用这些方法时，需要提前获取到 Condition 对象关联的锁。
 * Condition 对象是由Lock对象（调用Lock对象的 newCondition() 方法） 创建出来的，换句话说， Condition 是依赖 Lock 对象的。
 * 每个 Condition 对象都包含着一个等待队列
 * Condition.await() 该线程将会释放锁、构造成节点加入等待队列并进入等待状态
 */
public class ConditionUseCase {

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void conditionWait() throws InterruptedException {
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }
    }

    public void conditionSignal() throws InterruptedException {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
