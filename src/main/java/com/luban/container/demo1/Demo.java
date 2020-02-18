package com.luban.container.demo1;

import java.util.concurrent.TimeUnit;

/**
 * synchronized加在静态方法上锁的是类对象,证明
 */
public class Demo {

    public synchronized static void test1(){
        System.out.println("test1 start");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test1 end");
    }

    public void test2(){
        synchronized (getClass()){
            System.out.println("test2 start");
        }
    }

    public static void main(String[] args){
        Demo demo = new Demo();
        new Thread(Demo :: test1,"test1").start();
        new Thread(demo :: test2,"test2").start();
    }

}
