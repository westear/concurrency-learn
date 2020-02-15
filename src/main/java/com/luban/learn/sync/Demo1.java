package com.luban.learn.sync;

/**
 * synchronized 关键字
 *
 * @projectName: concurrency
 * @package: com.luban.learn.sync
 * @className: Demo1
 * @author: Qinyunchan
 * @date: 2020/2/13  2:08 下午
 * @version: 1.0
 */
public class Demo1 {

    private int count = 10;
    /**
     *           存在栈里       存在堆里
     */
    private Object object = new Object();

    /**
     * 这种加synchronized的方法比较浪费资源，
     * 假设多个线程调用该test方法，则每次执行都需要new一个object对象
     */
    public void test() {
        //锁的是 object的类实例
        synchronized (object){
            count--;
            System.out.println(Thread.currentThread().getName()+"count="+count);
        }
    }
}
