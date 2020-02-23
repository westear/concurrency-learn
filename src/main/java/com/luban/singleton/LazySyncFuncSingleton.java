package com.luban.singleton;

import java.util.concurrent.TimeUnit;

/**
 * 懒汉模式，同步方法可以保证单例对象的实例安全，但是退化成串行执行，性能下降太多
 */
public class LazySyncFuncSingleton {
    private static LazySyncFuncSingleton instance = null;

    //不允许调用者通过构造器 new 一个单例对象的实例
    //在单例类的私有构造器里完成实例化具体操作，如数据可连接，等
    private LazySyncFuncSingleton() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static LazySyncFuncSingleton getInstance() {
        if (null == instance)
            instance = new LazySyncFuncSingleton();
        return instance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                System.out.println(LazySyncFuncSingleton.getInstance());
            }).start();
        }
    }
}
