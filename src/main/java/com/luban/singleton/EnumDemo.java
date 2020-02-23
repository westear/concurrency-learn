package com.luban.singleton;

/**
 * 加载时就实例化
 * 枚举实例创建是线程安全的，在任何情况下，它都是一个单例
 */
public enum EnumDemo {
    //常量，在加载的时候实例化一次，方法区
    A,B,C,D;
    int i=10;
    int j=20;
    public static void m1(){
        System.out.println("method");
    }

    public static void main(String[] args) {
        A.m1();
        B.m1();
        C.m1();
        D.m1();


        System.out.println(A.getClass().getName());
        System.out.println(B.getClass().getName());
        System.out.println(C.getClass().getName());
        System.out.println(D.getClass().getName());
    }
}
