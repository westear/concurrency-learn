package com.westear.concurrency.lockAndAQS.condition;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过一个有界队列的示例来深入了解Condition的使用方式.
 * 有界队列，当队列为空时，队列的获取操作将会阻塞获取线程，直到队列中有新增元素。
 * 当队列已满时，队列的插入操作将会阻塞插入线程，直到队列出现了"空位"
 */
@Slf4j
public class BoundedQueue<T> {
    private Object[] items;
    //添加下标，删除的下标和数组当前数量
    private int addIndex, removeIndex, count;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public BoundedQueue(int size) {
        items = new Object[size];
    }

    //添加一个元素，如果数组满，则添加线程进入等待状态，直到有“空位”
    public void add(T t) throws InterruptedException {
        lock.lock();
        try {
            //队列已满
            while (count == items.length) {
                //阻塞判断队列已满的条件
                notFull.await();
            }
            //添加元素
            items[addIndex] = t;
            //添加元素满后，数组下标复位
            if(++addIndex == items.length) {
                addIndex = 0;
            }
            //元素增加
            ++count;
            //唤醒判断元素不为空的条件
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    //由头部删除一个元素，如果数组为空，则删除线程进入状态，直到有新添加元素
    @SuppressWarnings("unchecked")
    public T remove() throws InterruptedException {
        lock.lock();
        try {
            //元素为空
            while (count == 0) {
                //阻塞判断元素为空的条件
                notEmpty.await();
            }
            //取出队列元素
            Object x = items[removeIndex];
            if(++removeIndex == items.length) {
                removeIndex = 0;
            }
            //队列元素减少
            --count;
            //唤醒判断队列不满的条件
            notFull.signal();
            return (T) x;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException{
        BoundedQueue<Integer> queue = new BoundedQueue<>(5);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    Integer num = new Random().nextInt(10);
                    queue.add(num);
                    log.info("thread-{} add num = {}",Thread.currentThread().getName(),("add"+num));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(2);

        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {
                    Integer num = queue.remove();
                    log.info("thread-{} remove num = {}",Thread.currentThread().getName(),("remove"+num));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
