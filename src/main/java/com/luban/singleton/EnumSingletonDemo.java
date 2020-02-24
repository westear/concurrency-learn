package com.luban.singleton;

import java.util.concurrent.TimeUnit;

/**
 * 枚举类和静态内部类的结合
 */
public class EnumSingletonDemo {

    //在单例类的私有构造器里完成实例化具体操作，如数据可连接，等
    private EnumSingletonDemo(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //enum类是静态内部类，实现延迟加载
    private enum EnumHolder{
        INSTANCE;

        private  EnumSingletonDemo instance=null;

        //在单例类的私有构造器里完成实例化具体操作，如数据可连接，等
        EnumHolder(){
            instance=new EnumSingletonDemo();
        }
        private EnumSingletonDemo getInstance(){
            return instance;
        }
    }
    //懒加载:
    // 调用该方法时，实例化 EnumHolder, 一旦实例化 EnumHolder，由于 EnumHolder 是枚举类，枚举实例创建是线程安全的，在任何情况下，它都是一个单例
    //所以确保实例化 EnumSingletonDemo 线程安全
    public static EnumSingletonDemo  getInstance(){
        return EnumHolder.INSTANCE.instance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++){
            new Thread(()-> System.out.println(EnumSingletonDemo.getInstance())).start();
        }
    }
}
