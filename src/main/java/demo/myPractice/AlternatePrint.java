package demo.myPractice;

import java.util.concurrent.TimeUnit;

/**
 * 两个线程交替打印的例子
 * 使用 wait() 和 notify() 实现两个线程交替打印 B-A-B-A.....
 */
public class AlternatePrint {

    private static int num = 10;

    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();

        Thread threadOne = new Thread(new One(lock),"threadOne-");
        Thread threadTwo = new Thread(new Two(lock),"threadTwo-");

        //由于线程逻辑控制得比较严格，哪个线程先启动都不会影响打印顺序
        threadOne.start();
        TimeUnit.SECONDS.sleep(1);
        threadTwo.start();
    }

    private static class One implements Runnable {

        private final Object lock;

        public One(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (num > 0) {
                    if (num % 2 == 1) {
                        try {
                            System.out.println(Thread.currentThread().getName()+"A-"+num);
                            num--;
                            lock.notify(); //唤醒 threadTwo线程
                            lock.wait(); //挂起 threadOne线程， 释放锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        lock.notify(); //唤醒 threadTwo线程
                        try {
                            lock.wait(); //挂起 threadOne线程， 释放锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("threadOne finish");
                lock.notify();  //唤醒 threadTwo,使得threadTwo线程可以执行完成
            }   //释放锁 相当于 lock.wait()
        }
    }

    private static class Two implements Runnable {

        private final Object lock;

        public Two(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (num > 0) {
                    if(num % 2 == 0) {
                        try {
                            System.out.println(Thread.currentThread().getName()+"B-"+num);
                            num--;
                            lock.notify(); //唤醒 threadOne线程
                            lock.wait(); //挂起 threadTwo线程， 释放锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        lock.notify(); //唤醒 threadOne线程
                        try {
                            lock.wait(); //挂起 threadTwo线程， 释放锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("threadTwo finish");
                lock.notify(); //唤醒 threadOne,使得threadOne线程可以执行完成
            }   //释放锁 相当于 lock.wait()
        }
    }
}