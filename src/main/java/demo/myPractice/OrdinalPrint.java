package demo.myPractice;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 依次的使用三个线程进行顺序打印，如：A-B-C 或者 C-B-A
 * 使用 Condition 实现
 */
public class OrdinalPrint {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition conditionOne = lock.newCondition();
    private static Condition conditionTwo = lock.newCondition();
    private static Condition conditionThree = lock.newCondition();
    private static int count = 9;

    private static class One implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                while (count > 0) {
                    if(count % 3 == 1) {    //周期内第3位打印
                        System.out.println(Thread.currentThread().getName() + "A-" + count);
                        count--;
                    }else {
                        conditionThree.signal(); //如果当前不应该是线程1来打印，那么也要唤醒线程3，有可能是线程3的任务
                    }
                    conditionTwo.signal(); //当前线程1打印完毕后唤醒线程2
                    conditionOne.await();  //线程1挂起，释放锁
                }
                System.out.println("threadOne finish");
                conditionTwo.signal(); //唤醒线程2，使得跳出循环后可以执行完线程2
                conditionThree.signal(); //唤醒线程3，使得跳出循环后可以执行完线程3
            }catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class Two implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                while (count > 0) {
                    if(count % 3 == 2) {    //周期内第2位打印
                        System.out.println(Thread.currentThread().getName() + "B-" + count);
                        count--;
                    }else {
                        conditionOne.signal();
                    }
                    conditionThree.signal();
                    conditionTwo.await();
                }
                System.out.println("threadTwo finish");
                conditionThree.signal();
                conditionOne.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class Three implements Runnable {
        @Override
        public void run() {
            lock.lock();
            try {
                while (count > 0) {
                    if(count % 3 == 0) {    //周期内第1位打印
                        System.out.println(Thread.currentThread().getName() + "C-" + count);
                        count--;
                    }else {
                        conditionTwo.signal();
                    }
                    conditionOne.signal();
                    conditionThree.await();
                }
                System.out.println("threadThree finish");
                conditionOne.signal();
                conditionTwo.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {

        Thread threadA = new Thread(new One(), "threadOne-");
        Thread threadB = new Thread(new Two(), "threadTwo-");
        Thread threadC = new Thread(new Three(), "threadThree-");

        //由于线程逻辑控制得比较严格，哪个线程先启动都不会影响打印顺序
        threadB.start();
        threadA.start();
        threadC.start();
    }
}
