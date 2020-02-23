package com.luban.singleton;

import java.util.concurrent.TimeUnit;

/**
 * 静态内部类保证单例线程安全
 */
public class HolderDemo {

    //在单例类的私有构造器里完成实例化具体操作，如数据可连接，等
    private HolderDemo(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class Holder{
        //如何保证 instance 实例化时是线程安全的？
        //在《深入理解JAVA虚拟机》中，有这么一句话:
        //  虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确地加锁、同步，
        //  如果多个线程同时去初始化一个类，那么只会有一个线程去执行这个类的<clinit>()方法，其他线程都需要阻塞等待，直到活动线程执行<clinit>()方法完毕。
        //  如果在一个类的<clinit>()方法中有耗时很长的操作，就可能造成多个进程阻塞
        //  (需要注意的是，其他线程虽然会被阻塞，但如果执行<clinit>()方法后，其他线程唤醒之后不会再次进入<clinit>()方法。
        //  同一个加载器下，一个类型只会初始化一次。)。在实际应用中，这种阻塞往往是很隐蔽的。

        // 虚拟机首先执行的是类加载初始化过程中的 <clinit>() 方法，也就是静态变量赋值以及静态代码块中的代码，
        // 如果 <clinit>() 方法中触发了对象的初始化，也就是 <init>() 方法，那么会进入执行 <init>() 方法，执行 <init>() 方法完成之后，再回来继续执行 <clinit>() 方法。
        // 因为 private static HolderDemo instance=new HolderDemo(); 是 Holder 类中的静态变量，所以会触发 JVM 执行 <clinit>() 方法。
        // 这一步和“饿汉模式”是同理的，这也是“饿汉模式”能保证线程安全的原因
        private static HolderDemo instance=new HolderDemo();
    }

    //懒加载, 并不是加载 HolderDemo 时就实例化。外部类加载时并不需要立即加载内部类，内部类不被加载则不去初始化 instance，故而不占内存
    //未使用 synchronized
    //调用 Holder.instance 时实例化

    //类加载时机：JAVA虚拟机在有且仅有的5种场景下会对类进行初始化。
    //1.遇到new、getstatic、setstatic或者invokestatic这4个字节码指令时，对应的java代码场景为：
    //    一、new一个关键字或者一个实例化对象时、
    //    二、读取或设置一个静态字段时(final修饰、已在编译期把结果放入常量池的除外)、
    //    三、调用一个类的静态方法时。
    //2.使用java.lang.reflect包的方法对类进行反射调用的时候，如果类没进行初始化，需要先调用其初始化方法进行初始化。
    //3.当初始化一个类时，如果其父类还未进行初始化，会先触发其父类的初始化。
    //4.当虚拟机启动时，用户需要指定一个要执行的主类(包含main()方法的类)，虚拟机会先初始化这个类。
    //5.当使用JDK 1.7等动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果REF_getStatic、REF_putStatic、REF_invokeStatic的方法句柄，并且这个方法句柄所对应的类没有进行过初始化，则需要先触发其初始化。
    //这5种情况被称为是类的主动引用，注意，这里《虚拟机规范》中使用的限定词是"有且仅有"，那么，除此之外的所有引用类都不会对类进行初始化，称为被动引用。静态内部类就属于被动引用的行列。

    // public static HolderDemo getInstance(){return Holder.instance;} 满足 “调用一个类的静态方法时” 对该类(Holder)进行初始化
    public static HolderDemo getInstance(){
        return Holder.instance;
    }

    //广泛的一种单例模式

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++){
            new Thread(()-> System.out.println(HolderDemo.getInstance())).start();
        }
    }
}
