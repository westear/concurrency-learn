package com.luban.learn.sync;

/**
 * synchronized 关键字
 *
 * @projectName: concurrency
 * @package: com.luban.learn.sync
 * @className: Demo2
 * @author: Qinyunchan
 * @date: 2020/2/13  2:20 下午
 * @version: 1.0
 */
public class Demo2 {

    private int count = 10;

    /**
     * 这种加synchronized的方法相比demo1稍微好些，
     * 不需要每次new一个对象来锁，锁的是调用Test方法的对象本身new Demo2()
     * 就该方法而言，等价于整个方法加锁:
     *
     * public synchronized void Test(){
     *     count--;
     *     System.out.println(Thread.currentThread().getName()+"count="+count);
     * }
     */
    public void Test() {
        synchronized (this){
            count--;
            System.out.println(Thread.currentThread().getName()+"count="+count);
        }
    }
}
