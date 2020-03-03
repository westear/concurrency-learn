package com.westear.concurrency.lockAndAQS;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 该程序说明读写锁 ReentrantReadWriteLock 的使用方式
 * ReentrantReadWriteLock 定义了获取读锁和写锁的两个方法，即 readLock() 方法 和 writeLock() 方法
 * 用读写锁的读锁和写锁来保证 map 是安全的
 *
 * 读写锁的设计思想
 * 按位切割，在变量上维护多种状态，以整型变量为例，高16位表示读（包括了重入了几次），低16位表示写（包括了重入了几次）
 * 写锁是支持重入但排他锁
 *      写锁重入时需要判断是否存在读锁，因为要确保写锁对所有的读锁可见，所以存在读锁的时候，不允许写锁重入。
 *      而一旦获取写锁，当前线程的后续读写访问均被阻塞
 * 读锁是支持重入且共享锁
 *      在没有其他线程访问时，读锁总会被成功获取。
 *      如果当前线程获取读锁时，写锁已经被其他线程获取，则进入等待状态。
 *      如果当前线程尝试获取读锁时，当前线程获取了写状态或者写状态未被获取，则当前线程增加读状态
 */
public class ReentrantReadWriteLockDemo {
    static Map<String, Object> map = new HashMap<>();
    static ReentrantReadWriteLock rw1 = new ReentrantReadWriteLock();
    //读锁
    static Lock r = rw1.readLock();
    //写锁
    static Lock w = rw1.writeLock();

    //获取一个key对应的value
    public static Object get(String key) {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    //设置key对应的value,并返回旧的value
    public static Object put(String key, Object value) {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }

    //清空所有的内容
    public static void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }
}
