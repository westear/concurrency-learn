package com.westear.concurrency.example.lock;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.westear.concurrency.annoations.ThreadSafe;

/**
 * ReentrantReadWriteLock
 * @author SAGE
 *
 */
@ThreadSafe
public class LockExample2 {

	private final Map<String, Data> map = new TreeMap<>();

	//在没有读锁或者写锁状态下允许执行写操作
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock readLock = lock.readLock();

    private final Lock writeLock = lock.writeLock();

    public Data get(String key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public Set<String> getAllKeys() {
        readLock.lock();
        try {
            return map.keySet();
        } finally {
            readLock.unlock();
        }
    }

    public Data put(String key, Data value) {
    	//如果读操作很多，那么写锁操作可能一直处于线程饥饿状态
        writeLock.lock();
        try {
            return map.put(key, value);
        } finally {
            readLock.unlock();
        }
    }

    class Data {

    }
}