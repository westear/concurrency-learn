package com.westear.concurrency.javaThread;

/**
 * 创建10个线程，编号0-9，每个线程调用前一个线程的 join() 方法，也就是线程0结束了，线程1才能从 join() 方法中返回，
 * 而线程0需要等待 main 线程结束
 */
public class Join {
    public static void main(String[] args) {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            //每个线程拥有前一个线程的引用，需要等待前一个线程终止，才能从等待中返回
            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            thread.start();
            previous = thread;
        }
    }

    static class  Domino implements Runnable {
        private Thread thread;
        public Domino(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                //此时的 thread 是 previous, 所以 要等 previous 执行完. 底层调用了 wait()
                thread.join();
                //线程 previous 执行完，可以返回当前线程结果, 以此保证循环的线程按顺序执行. 底层调用了notifyAll()
            } catch (InterruptedException e) {

            }
            System.out.println(Thread.currentThread().getName() + "terminate.");
        }
    }
}
