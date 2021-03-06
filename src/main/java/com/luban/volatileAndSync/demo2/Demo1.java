package com.luban.volatileAndSync.demo2;

import java.util.concurrent.TimeUnit;

/**
 * 锁对象的改变
 * 锁定某对象o，如果o的属性发生改变，不影响锁的使用
 * 但是如果o变成另外一个对象，则锁定的对象发生改变
 * 应该避免将锁定对象的引用变成另外一个对象
 */
public class Demo1 {

    Object o = new Object();

    public void test(){
        synchronized (o) {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            }
        }
    }

    public static void main(String[] args) {
        Demo1 demo = new Demo1();

        new Thread(demo :: test, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t2 = new Thread(demo :: test, "t2");

        demo.o = new Object();
        //t2能否执行？o已经是另一个实例了，t2和t1的锁不是同一把，所以是t2是可以执行的
        t2.start();
    }

}
