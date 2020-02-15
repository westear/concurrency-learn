package com.luban.learn.sync;

/**
 * synchronized 关键字，静态方法加锁
 *
 * @projectName: concurrency
 * @package: com.luban.learn.sync
 * @className: Demo4
 * @author: Qinyunchan
 * @date: 2020/2/13  2:28 下午
 * @version: 1.0
 */
public class Demo3 {

    private static int count = 10;

    public synchronized static void test(){
        count--;
        System.out.println(Thread.currentThread().getName()+"count="+count);
    }

    /**
     * 对于静态方法加锁，锁的是类的字节码(Demo3.class),而不是类对象的实例
     */
    public static void test2(){
        synchronized (Demo3.class){
            count--;
        }
    }
}
