package com.luban.volatileAndSync.demo13;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 一道面试题：实现一个容器，提供两个方法，add,size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，
 * 线程2给出提示并结束
 *
 * 这里虽然T2能够及时收到消息唤醒，但是wait会释放锁，notify不会释放锁，所以T1线程结束后
 * T2线程才执行完成
 *
 * notify() 或 notifyAll() 方法调用后， 等待线程依旧不会从 wait() 返回，需要调用 notify() 或者 notifyAll() 的线程释放锁之后，等待线程才有机会从 wait() 返回
 * notify() 方法将等待队列中的一个等待线程从等待队列中移到同步队列中，而 notifyAll() 方法则是将等待队列中的所有线程全部转移到同步队列， 被移动的线程状态由 WAITING 变为 BLOCKED
 *  *      5.从 wait() 方法返回的前提是获得了调用对象的锁
 */
public class Container3 {

    //添加列表容器可见性
    volatile List lists = new ArrayList();

    public void add(Object o){
        lists.add(o);
    }

    public int size(){
        return lists.size();
    }

    public static void main(String[] args) {
        Container3 c = new Container3();
        Object lock = new Object();

        new Thread(()->{
            synchronized (lock) {
                System.out.println("t2启动");
                if (c.size() != 5) {
                    try {
                        lock.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t2结束");
            }
        }," t2").start();

        new Thread(()->{
            System.out.println("t1启动");
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    c.add(new Object());
                    System.out.println("add " + i);

                    if (c.size() == 5) {
                        lock.notify();
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "t1").start();

    }

}
