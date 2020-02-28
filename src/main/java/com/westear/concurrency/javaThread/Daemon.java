package com.westear.concurrency.javaThread;

import java.util.concurrent.TimeUnit;

/**
 * Daemon 是一种支持线程，当一个虚拟机中不存在非Daemon线程的时候，Java虚拟机将会退出。
 * 但在Java虚拟机退出时 Daemon 线程中的 finally 块并不一定执行，本程序对此进行演示
 * 所以不能依靠Daemon线程的finally程序块中的代码来确保执行关闭或者清理资源的逻辑
 */
public class Daemon {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner());
        thread.setDaemon(true);
        thread.start();
    }

    static class DaemonRunner implements Runnable {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            }catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                System.out.println("DaemonThread finally run");
            }
        }
    }
}
