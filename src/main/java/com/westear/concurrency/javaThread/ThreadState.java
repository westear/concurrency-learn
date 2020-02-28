package com.westear.concurrency.javaThread;

import java.util.concurrent.TimeUnit;

/**
 * 使用 jstack (在JDK的bin目录下)工具尝试查看以下代码运行时的线程信息，可以更加深入的了解线程状态
 * 运行该程序，并在终端输入jps命令，看到程序对应进程ID，再输入 “jstack 进程ID”, 查看栈输出：
 *
 * "BlockedThread-2" #15 prio=5 os_prio=0 tid=0x000000001b0fa800 nid=0x6840 waiting for monitor entry [0x000000001bccf000]
 *    java.lang.Thread.State: BLOCKED (on object monitor)
 * 说明： BlockedThread-2 线程阻塞在获取 Blocked.class 对象的锁上[waiting for monitor entry]，线程当前为阻塞状态
 *
 * "BlockedThread-1" #14 prio=5 os_prio=0 tid=0x000000001b0bb800 nid=0x4b18 waiting on condition [0x000000001bbcf000]
 *    java.lang.Thread.State: TIMED_WAITING (sleeping)
 * 说明： BlockedThread-1 线程获取到了 Blocked.class 的锁，由于一直sleep,所以线程当前为超时等待状态[waiting on condition],但sleep不会导致当前线程释放锁
 *
 *"WaitingThread" #13 prio=5 os_prio=0 tid=0x000000001b0be000 nid=0x666c in Object.wait() [0x000000001bacf000]
 *    java.lang.Thread.State: WAITING (on object monitor)
 * 说明： WaitingThread 线程在 Waiting.class 对象上等待[in Object.wait()],等待其他线程唤醒（因为使用了Thread.wait();）
 *
 * "TimeWaitingThread" #12 prio=5 os_prio=0 tid=0x000000001b000800 nid=0x570c waiting on condition [0x000000001b9ce000]
 *    java.lang.Thread.State: TIMED_WAITING (sleeping)
 * 说明： TimeWaitingThread 线程处于超时等待状态[waiting on condition]
 *
 * Java线程的状态:
 *      NEW : 线程被构建，但未调用start()方法
 *      RUNNING: 运行状态，Java线程将操作系统中的就绪和运行两种状态统称为“运行态”
 *      BLOCKED: 阻塞状态，表示线程阻塞于锁
 *      WAITING: 等待状态，表示线程进入等待状态，进入该状态表示当前线程需要等待其他线程做出一些特定动作（通知或中断）
 *      TIME_WAITING: 超时等待状态，该状态不同于WAITING,它可以在指定的时间自行返回
 *      TERMINATED: 终止状态，表示当前线程已经执行完毕
 */
public class ThreadState {
    public static void main(String[] args) {
        new Thread(new TimeWaiting(), "TimeWaitingThread").start();
        new Thread(new Waiting(), "WaitingThread").start();
        //使用两个Blocked线程，一个获取锁成功，另一个被阻塞
        new Thread(new Blocked(), "BlockedThread-1").start();
        new Thread(new Blocked(), "BlockedThread-2").start();
    }

    //该线程不断的进行睡眠
    static class TimeWaiting implements Runnable {
        @Override
        public void run() {
            while (true){
                SleepUtils.second(100);
            }
        }
    }

    //该线程在Waiting.class上等待
    static class Waiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //该线程在Blocked.class实例上加锁后，100秒内不会释放该锁
    static class Blocked implements Runnable {
        @Override
        public void run() {
            synchronized (Blocked.class) {
                while (true) {
                    //100秒不释放锁
                    SleepUtils.second(100);
                }
            }
        }
    }

    private  static class SleepUtils {
        public static void second(long seconds) {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
