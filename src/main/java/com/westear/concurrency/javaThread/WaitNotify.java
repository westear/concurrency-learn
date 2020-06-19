package com.westear.concurrency.javaThread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 创建两个线程—— WaitThread 和 NotifyThread ,
 * 前者检查flag值是否为false,如果符合要求，进行后续操作，否则在lock上等待，
 * 后者在睡眠了一段时间后对 lock 进行通知。
 * 说明使用 wait(), notify(), notifyAll() 的一些细节:
 *      1.使用 wait()， notify(), notifyAll() 时需要先对调用对象加锁
 *      2.调用 wait() 方法后，线程状态由 RUNNING 变为 WAITING, 并将当前线程放置到对象的等待队列
 *      3.notify() 或 notifyAll() 方法调用后， 等待线程依旧不会从 wait() 返回，需要调用 notify() 或者 notifyAll() 的线程释放锁之后，等待线程才有机会从 wait() 返回
 *      4.  notify() 方法将等待队列中的一个等待线程从等待队列中移到同步队列中，
 *          而 notifyAll() 方法则是将等待队列中的所有线程全部转移到同步队列， 被移动的线程状态由 WAITING 变为 BLOCKED
 *      5.从 wait() 方法返回的前提是获得了调用对象的锁
 * 以上可以说明，等待/通知机制依托于同步机制，离不开锁
 *
 *
 * 等待/通知 的代码实现范式如下:
 * 等待方：
 *      synchronized (对象) {
 *          while(条件不满足) {
 *              对象.wait();
 *          }
 *          对应处理逻辑
 *      }
 *
 * 通知方：
 *      synchronized(对象){
 *          改变条件
 *          对象.notifyAll(); 或者 对象.notify();
 *      }
 */
public class WaitNotify {
    static boolean flag = true;
    static final Object lock = new Object();

    public static void main(String[] args) throws Exception {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new Notify(),"NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable {
        @Override
        public void run() {
            //加锁，拥有lock的锁的Monitor
            synchronized (lock) {
                //当条件不满足时，继续wait, 同时释放了lock的锁
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + "flag is true. wait @"
                                + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //条件满足时，完成工作
                System.out.println(Thread.currentThread() + "flag is false. running @"
                        + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }

    static class Notify implements Runnable {
        @Override
        public void run() {
            //加锁，拥有lock的锁的Monitor
            synchronized (lock) {
                //获取lock的锁，然后进行通知，通知不会释放lock的锁，直到当前线程释放了lock的锁，WaitThread才能从wait方法中返回
                System.out.println(Thread.currentThread() + " hold lock. notify @ "
                        + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                lock.notifyAll();
                flag = false;
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //再次加锁
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock again. sleep @ "
                        + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
