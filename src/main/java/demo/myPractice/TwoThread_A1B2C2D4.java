package demo.myPractice;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用两个线程交替输出 A1 B2 C2 D4 ....., 线程一输出字母，线程二输出数字
 */
public class TwoThread_A1B2C2D4 {

    private static char letter = 'A';
    private static int number = 1;
    private static volatile boolean isLetterPrint = true;

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition letterCondition = lock.newCondition();
    private static Condition numberCondition = lock.newCondition();

    public static void main(String[] args) {
        new Thread(new LetterTask(), "letterThread").start();
        new Thread(new NumberTask(), "numberThread").start();
    }

    private static class LetterTask implements Runnable {

        @Override
        public void run() {
            lock.lock();
            try {
                while (isLetterPrint) {
                    System.out.print(letter);
                    letter++;
                    isLetterPrint = false;
                    TimeUnit.SECONDS.sleep(1);
                    numberCondition.signal();
                    letterCondition.await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class NumberTask implements Runnable {

        @Override
        public void run() {
            lock.lock();
            try {
                while (!isLetterPrint) {
                    System.out.print(number + " ");
                    number++;
                    isLetterPrint = true;
                    TimeUnit.SECONDS.sleep(1);
                    letterCondition.signal();
                    numberCondition.await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }
}
