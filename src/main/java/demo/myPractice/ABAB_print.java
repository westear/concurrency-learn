package demo.myPractice;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ABAB_print {

    private static volatile boolean abFlag = true;

    private static AtomicBoolean aPrint = new AtomicBoolean(Boolean.TRUE);

    private static Object mutex = new Object();

    public static void main(String[] args) {

        Thread aThread = new Thread(new A_Thread(mutex, aPrint), "a_print_thread");
        Thread bThread = new Thread(new B_Thread(mutex, aPrint), "b_print_thread");

        aThread.start();
        bThread.start();
    }

    private static class A_Thread implements Runnable {
        private final Object mutex;
        private AtomicBoolean aPrint;

        public A_Thread(Object mutex, AtomicBoolean aPrint) {
            this.mutex = mutex;
            this.aPrint = aPrint;
        }

        @Override
        public void run() {
            synchronized (mutex) {
                while (abFlag) {    //可以使用AtomicBoolean替代
                    System.out.println(Thread.currentThread().getName() + " execute ");
                    aPrint.set(Boolean.FALSE);
                    abFlag = false;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        mutex.notify();
                        mutex.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private static class B_Thread implements Runnable {
        private final Object mutex;
        private AtomicBoolean bPrint;

        public B_Thread(Object mutex, AtomicBoolean bPrint) {
            this.mutex = mutex;
            this.bPrint = bPrint;
        }

        @Override
        public void run() {
            synchronized (mutex) {
                while (!abFlag) {   //可以使用AtomicBoolean替代
                    System.out.println(Thread.currentThread().getName() + " execute ");
                    aPrint.set(Boolean.TRUE);
                    abFlag = true;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        mutex.notify();
                        mutex.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }


}
