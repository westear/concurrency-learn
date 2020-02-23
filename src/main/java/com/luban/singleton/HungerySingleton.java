package com.luban.singleton;

import java.util.concurrent.TimeUnit;

/**
 * 饿汉模式生成单例，类加载的时候就产生的实例对象，线程安全
 * 缺点： 没有懒加载，长时间在内存中没有被使用
 */
public class HungerySingleton {

    private byte[] data=new byte[1024];

    //加载的时候就产生的实例对象,ClassLoader加载
    private static HungerySingleton instance=new HungerySingleton();

    //在单例类的私有构造器里完成实例化具体操作，如数据可连接，等
    private HungerySingleton(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //返回实例对象
   public static HungerySingleton getInstance(){
        return instance;
   }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                //输出同一个对象实例地址，证明是单例的生成了一个对象实例
                System.out.println(HungerySingleton.getInstance());
            }).start();
        }
    }
}
