package com.luban.singleton;

/**
 * 非懒加载
 */
public enum EnumSingleton {
    INSTANCE;
    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
}

//holder
//枚举
//DCL
