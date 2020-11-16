package demo.myPractice;

import com.westear.concurrency.javaThread.Interrupted;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消费者生产者模型
 */
public class ConsumerProducerDemo {
    private static int SIZE = 10;
    private static volatile int index = 0;
    private static volatile LinkedList<Integer> queue = new LinkedList<>();
    private static Lock lock = new ReentrantLock();
    private static Condition consumerCondition = lock.newCondition();
    private static Condition producerCondition = lock.newCondition();

    public static void main(String[] args) {

        Runnable consumerRunner = new Runnable() {

            private volatile boolean running = true;

            @Override
            public void run() {
                while (running) {
                    lock.lock();
                    try {
                        if (queue.size() == 0) {
                            System.out.println(Thread.currentThread().getName() + " 想要消费，但队列此时为空 size: " + queue.size());
                            producerCondition.signalAll();
                            consumerCondition.await();
                        }
                        System.out.println(Thread.currentThread().getName() + " 消费 " + queue.poll() + " size: " + queue.size());
                        TimeUnit.SECONDS.sleep(1);
                    }catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        lock.unlock();
                    }finally {
                        lock.unlock();
                    }
                }
            }

            public void stop() {
                running = false;
            }
        };

        Runnable producerRunner = new Runnable() {

            private volatile boolean running = true;

            @Override
            public void run() {
                while (running) {
                    lock.lock();
                    try {
                        if (queue.size() == SIZE) {
                            System.out.println(Thread.currentThread().getName() + " 想要生产，但队列此时已满 size: " + queue.size());
                            consumerCondition.signalAll();
                            producerCondition.await();
                        }
                        queue.add(index++);
                        System.out.println(Thread.currentThread().getName() + " 生产 " + index + " size: " + queue.size());
                        TimeUnit.SECONDS.sleep(1);
                    }catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        lock.unlock();
                    }finally {
                        lock.unlock();
                    }
                }
            }

            public void stop() {
                running = false;
            }
        };

        ExecutorService execute = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            execute.execute(consumerRunner);
        }
        for (int i = 0; i < 2; i++) {
            execute.execute(producerRunner);
        }

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
