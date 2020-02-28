package com.luban.aqs;

/**
 * 该示例的 MyLock锁 尝试方法：tryAcquire 实现了多线程争锁避免死锁机制，所以不会造成死锁,否则a,b两个方法对同一个锁的竞争会造成死锁现象
 */
public class Demo02 {

    private MyLock lock=new MyLock();
    private int m=0;
    public void a(){
        lock.lock();
        System.out.println("a");
        b();
        lock.unlock();
    }
    public void b(){
        lock.lock();
        System.out.println("b");
        lock.unlock();
    }

    public static void main(String[] args) {
        Demo02 demo=new Demo02();
        new Thread(()->{
            demo.a();
        }).start();
    }
}
