package com.luban.singleton;

import java.util.concurrent.TimeUnit;

/**
 *  懒汉模式，不能保证实例对象的唯一性
 *  同步代码块并不能保证线程安全
 *
 */
public class LazySyncCodeSingleton {
    private static LazySyncCodeSingleton instance=null;

    //在单例类的私有构造器里完成实例化具体操作，如数据可连接，等
    private LazySyncCodeSingleton(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static LazySyncCodeSingleton getInstance(){
        //可能存在多个线程同时读到实例=null
        if(null==instance)
            //某一个线程竞争得到锁，实例化对象后，释放锁，唤醒等待获得锁的其他线程，
            //此时另一个线程得到锁后，再次实例化对象。造成对象实例的改变，线程不安全
            //所以说同步代码块并不能保证线程安全
            synchronized (LazySyncCodeSingleton.class){
                instance=new LazySyncCodeSingleton();
            }
        return instance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                System.out.println(LazySyncCodeSingleton.getInstance());
            }).start();
        }
    }
}
