package com.luban.singleton;

import java.util.concurrent.TimeUnit;

/**
 * 懒汉模式，不能保证实例对象的唯一性
 */
public class LazySingleton {
    private static LazySingleton instance=null;

    //在单例类的私有构造器里完成实例化具体操作，如数据可连接，等
    private LazySingleton(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static LazySingleton getInstance(){
        //1：读取instance的值
        if(null==instance)
            //2: 实例化instance
            instance=new LazySingleton();
        return instance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                System.out.println(LazySingleton.getInstance());
            }).start();
        }
    }
}
