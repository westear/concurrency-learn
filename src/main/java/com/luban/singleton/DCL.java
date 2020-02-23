package com.luban.singleton;
import java.net.Socket;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

/**
 * Double-check-locking,
 * 懒汉模式下,使用同步代码块提高同步性能
 * 不加 volatile 的情况下，可能会造成空指针，因为涉及到指令重排序问题
 * 所以  DCL instance 需要 volatile 修饰, 保证instance一定先实例化
 */
public class DCL {

    Connection conn;
    Socket socket;
    private volatile static DCL instance=null;

    //在单例类的私有构造器里完成实例化具体操作，如数据可连接，等
    private DCL(){

        //可能发生指令重排
        conn = null;
        socket = new Socket();
        DCL instance = new DCL();
        //可能性1:
        //以上可能发生指令重排，可能把 DCL instance = new DCL(); 放到第一条语句执行,instance已经不为空。
        // 此时另外的线程可能直接调用conn,socket,但conn,socket 还没有实例化完成，引起空指针异常

        //可能性2:
        //instance = new Singleton2(); 其实可以分为下面的步骤：
        //1.申请一块内存空间；
        //2.在这块空间里实例化对象；
        //3.instance的引用指向这块空间地址；
        //指令重排序很有可能不是按上面123步骤依次执行的。
        // 比如，先执行1申请一块内存空间，然后执行3步骤，instance的引用去指向刚刚申请的内存空间地址，
        // 那么，当它再去执行2步骤，判断instance时，由于instance已经指向了某一地址，它就不会再为null了，
        // 因此，也就不会实例化对象了。这就是所谓的指令重排序安全问题

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public  static DCL getInstance(){
        //如果后续线程执行到这条语句，发现instance已经不为空，则不需要竞争锁资源，跳过同步代码块，直接返回instance,提高性能
        if(null==instance) {
            //对于多个线程同时读到实例instance为空的情况，竞争锁资源
            //只有某一个线程获得锁资源，可以执行代码块内部的语句，其他线程在此阻塞等待锁的释放
            synchronized (DCL.class) {
                //对于多个线程同时读到实例instance为空的情况,第一个线程已经执行完同步代码块语句，实例化了instance,释放锁资源，
                //之前等待获得锁的其他线程中的某一个线程，竞争得到锁，也进入同步代码块执行如下语句
                //此时，再次读取方法区（共享内存）中的instance实例，发现instance实例此时已经被实例化，不为空，
                // 则跳过实例化的语句，直接返回第一个实例成功的线程产生的实例对象,接下来再次进入同步代码块的线程同理处理
                if (null == instance) {
                    //执行完上一条语句，已经通过两次判空，实现了double-check-locking
                    instance = new DCL();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                System.out.println(DCL.getInstance());
            }).start();
        }
    }
}

