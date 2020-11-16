package demo.myPractice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印A1,A2,A3......
 */
public class A1A2_print {

    private volatile static int nextNum = 1;

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                lock.lock();
                String name = Thread.currentThread().getName();
                int index = Integer.parseInt(name.substring(name.length()-1));
                while (nextNum != index) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                nextNum++;
                System.out.println(name);
                try{
                    TimeUnit.SECONDS.sleep(1);
                    condition.signalAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }finally {
                    lock.unlock();
                }
            }
        };

        for (int i = 1; i <= 5; i++) {
            new Thread(runnable, "A"+i).start();
        }

    }
}
