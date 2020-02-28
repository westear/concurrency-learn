package com.westear.concurrency.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 《Java并发编程》中的 CAS 例子,实现一个基于CAS线程安全的计数器方法 safeCount 和一个非线程安全的计数器 count
 */
public class Counter {

    private int i = 0;
    private AtomicInteger atomicI = new AtomicInteger(0);

    public static void main(String[] args) {
        final Counter cas = new Counter();
        List<Thread> ts = new ArrayList<>(100);
        long start = System.currentTimeMillis();

        for (int j = 0; j < 100; j++){
            Thread t = new Thread(()-> {
                for (int i = 0; i < 10000; i++) {
                    cas.count();
                    cas.safeCount();
                }
            });
            ts.add(t);
        }
        for (Thread t : ts){
            t.start();
        }

        //等待所有线程执行完成
        for (Thread t : ts){
            try {
                t.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println(cas.i);
        System.out.println(cas.atomicI.get());

        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 线程安全的计数器
     */
    private void safeCount(){
        for (;;){
            int i = atomicI.get();
            boolean success = atomicI.compareAndSet(i, ++i);
            if(success){
                break;
            }
        }
    }

    /**
     * 线程不安全的计数器
     */
    private void count(){
        i++;
    }
}
