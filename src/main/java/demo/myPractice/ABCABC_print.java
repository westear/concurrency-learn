package demo.myPractice;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ABCABC_print {

    private static volatile int num = 1;
    private static Lock lock = new ReentrantLock();
    private static Condition oneCondition = lock.newCondition();
    private static Condition twoCondition = lock.newCondition();
    private static Condition threeCondition = lock.newCondition();

    public static void main(String[] args) {

        OneThread oneThread = new OneThread(lock);
        TwoThread twoThread = new TwoThread(lock);
        ThreeThread threeThread = new ThreeThread(lock);

        new Thread(oneThread, "one-thread").start();
        new Thread(twoThread, "two-thread").start();
        new Thread(threeThread, "three-thread").start();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            oneThread.stop();
            twoThread.stop();
            threeThread.stop();
        }

        oneThread.stop();
        twoThread.stop();
        threeThread.stop();
    }

    private static class OneThread implements Runnable {

        private Lock lock;
        private volatile boolean stop = false;

        public OneThread(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                while (!stop && num % 3 == 1) {
                    System.out.println(Thread.currentThread().getName() + "：execute ......");
                    num = 2;
                    TimeUnit.SECONDS.sleep(1);
                    twoCondition.signal();
                    oneCondition.await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }

        public void stop() {
            Thread.currentThread().interrupt();
            stop = Thread.currentThread().isInterrupted();
        }
    }

    private static class TwoThread implements Runnable {

        private Lock lock;
        private volatile boolean stop = false;

        public TwoThread(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                while (!stop && num % 3 == 2) {
                    System.out.println(Thread.currentThread().getName() + "：execute ......");
                    num = 3;
                    TimeUnit.SECONDS.sleep(1);
                    threeCondition.signal();
                    twoCondition.await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }

        public void stop() {
            Thread.currentThread().interrupt();
            stop = Thread.currentThread().isInterrupted();
        }
    }

    private static class ThreeThread implements Runnable {

        private Lock lock;
        private volatile boolean stop = false;

        public ThreeThread(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                while (!stop && num % 3 == 0) {
                    System.out.println(Thread.currentThread().getName() + "：execute ......");
                    num = 1;
                    TimeUnit.SECONDS.sleep(1);
                    oneCondition.signal();
                    threeCondition.await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }

        public void stop() {
            Thread.currentThread().interrupt();
            stop = Thread.currentThread().isInterrupted();
        }
    }
}
