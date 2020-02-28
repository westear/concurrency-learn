package com.westear.concurrency.joinDemo;

/**
 * Thread.join 的原理测试示例
 * 保证线程的执行顺序
 */
public class JoinDemo extends Thread{

    int i;
    Thread previousThread; //上一个线程
    public JoinDemo(Thread previousThread,int i){
        this.previousThread=previousThread;
        this.i=i;
    }

    @Override
    public void run() {
        try {
            //调用上一个线程的join方法，演示的时候可以把这行代码注释掉
            previousThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("num:"+i);
    }
    public static void main(String[] args) {
        Thread previousThread=Thread.currentThread();
        for(int i=0;i<10;i++){
            JoinDemo joinDemo=new JoinDemo(previousThread,i);
            joinDemo.start();
            //当前线程等待上一个线程执行完后再将previousThread更新为当前线程
            previousThread=joinDemo;
        }
    }
}
