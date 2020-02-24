package com.luban.singleton;

import java.util.concurrent.TimeUnit;

/**
 * 非懒加载
 */
public enum EnumSingleton {
    INSTANCE;

    EnumSingleton(){
        //在单例类的私有构造器里完成实例化具体操作，如数据可连接，等
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
}

//holder
//枚举
//DCL
